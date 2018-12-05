package com.couchbase.lite;

import android.util.Log;

import com.couchbase.litecore.C4Log;

import java.util.ArrayList;

public final class ConsoleLogger implements Logger {
    private LogLevel _level = LogLevel.WARNING;
    private ArrayList<LogDomain> _domains = new ArrayList<>();

    ConsoleLogger() {
        _domains.add(LogDomain.ALL);
    }

    public ArrayList<LogDomain> getDomains() {
        return _domains;
    }

    public void setDomains(ArrayList<LogDomain> domains) {
        _domains = domains;
    }

    public void setLevel(LogLevel level) {
        if(level == null) {
            level = LogLevel.WARNING;
        }

        if(_level == level) {
            return;
        }

        _level = level;
        setCallbackLevel(level);
    }

    private void setCallbackLevel(LogLevel level) {
        LogLevel callbackLevel = level;
        Logger custom = Database.getLog().getCustom();
        if(custom != null) {
            if(custom.getLevel().compareTo(callbackLevel) < 0) {
                callbackLevel = custom.getLevel();
            }
        }

        C4Log.setCallbackLevel(callbackLevel.getValue());
    }

    @Override
    public LogLevel getLevel() {
        return _level;
    }

    @Override
    public void log(LogLevel level, LogDomain domain, String message) {
        if(level.compareTo(_level) < 0 || (!_domains.contains(domain) && !_domains.contains(LogDomain.ALL))) {
            return;
        }

        switch(level) {
            case DEBUG:
                Log.d("CouchbaseLite/" + domain.toString(), message);
                break;
            case VERBOSE:
                Log.v("CouchbaseLite/" + domain.toString(), message);
                break;
            case INFO:
                Log.i("CouchbaseLite/" + domain.toString(), message);
                break;
            case WARNING:
                Log.w("CouchbaseLite/" + domain.toString(), message);
                break;
            case ERROR:
                Log.e("CouchbaseLite/" + domain.toString(), message);
                break;
        }
    }
}
