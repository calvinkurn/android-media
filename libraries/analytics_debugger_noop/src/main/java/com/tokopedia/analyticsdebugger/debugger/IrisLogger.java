package com.tokopedia.analyticsdebugger.debugger;

import android.content.Context;

public class IrisLogger {

    private static IrisLoggerInterface instance;

    public static IrisLoggerInterface getInstance(Context context) {
        if (instance == null) {
            instance = new IrisLoggerInterface() {

                @Override
                public void putSendIrisEvent(String data, int rowCount) {

                }

                @Override
                public void putSaveIrisEvent(String data) {

                }

                @Override
                public void openSaveActivity() {

                }

                @Override
                public void openSendActivity() {

                }
            };
        }

        return instance;
    }
}
