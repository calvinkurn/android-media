package com.tokopedia.analyticsdebugger.debugger;

import android.content.Context;

/**
 * Created By : Jonathan Darwin on November 17, 2021
 */
public class SSELogger implements RealtimeNetworkLoggerInterface {

    @Override
    public void init(String generalInfo) {

    }

    @Override
    public void send(String event, String message) {

    }

    @Override
    public void send(String event) {

    }

    public RealtimeNetworkLoggerInterface getInstance(Context context) {
        return new SSELogger();
    }
}
