package com.tokopedia.analyticsdebugger.debugger.ws;

import android.content.Context;

import com.tokopedia.analyticsdebugger.debugger.WebSocketLogger;

public class PlayWebSocketLogger implements WebSocketLogger {

    private Context context;

    public PlayWebSocketLogger(Context context) {
        this.context = context;
    }

    @Override public void init(String data) {}
    @Override public void send(String event) {}
    @Override public void send(String event, String message) {}
}
