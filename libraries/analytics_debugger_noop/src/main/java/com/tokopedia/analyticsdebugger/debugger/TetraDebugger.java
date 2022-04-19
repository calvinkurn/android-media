package com.tokopedia.analyticsdebugger.debugger;

import android.content.Context;

import java.util.Map;

public interface TetraDebugger {

    /**
     * Initialize the test process, by validating the deviceId of the phone to testing server
     */
    void init();

    /**
     * send each data layer (currently only support gtm) to testing server
     */
    void send(Map<String, Object> data);

    void setUserId(String value);

    void cancel();

    class Companion {
        public static TetraDebugger instance(Context context) {
            return new TetraDebugger() {
                @Override
                public void init() {

                }

                @Override
                public void send(Map<String, Object> data) {

                }

                @Override
                public void setUserId(String value) {

                }

                @Override
                public void cancel() {

                }
            };
        }
    }


}