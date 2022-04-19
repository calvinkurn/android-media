package com.tokopedia.analyticsdebugger.debugger;

import android.content.Context;

/**
 * Created By : Jonathan Darwin on November 17, 2021
 */
public class SSELogger {

    private static RealtimeNetworkLoggerInterface instance;

    public static RealtimeNetworkLoggerInterface getInstance(Context context) {
        if(instance == null) {
            instance = new RealtimeNetworkLoggerInterface() {
                @Override
                public void init(String generalInfo) {

                }

                @Override
                public void send(String event, String message) {

                }

                @Override
                public void send(String event) {

                }
            };
        }
        return instance;
    }

}
