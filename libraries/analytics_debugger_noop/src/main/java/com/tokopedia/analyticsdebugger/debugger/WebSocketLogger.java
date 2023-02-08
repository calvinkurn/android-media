package com.tokopedia.analyticsdebugger.debugger;

public interface WebSocketLogger {
    void init(String data);
    void send(String event);
    void send(String event, String message);
}
