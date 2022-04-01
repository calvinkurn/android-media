package com.tokopedia.analyticsdebugger.debugger;

/**
 * Created By : Jonathan Darwin on November 17, 2021
 */

public interface RealtimeNetworkLoggerInterface {

    void init(String generalInfo);

    void send(String event, String message);

    void send(String event);
}
