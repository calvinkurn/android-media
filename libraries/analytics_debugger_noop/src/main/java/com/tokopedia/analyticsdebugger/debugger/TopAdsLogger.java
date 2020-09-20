package com.tokopedia.analyticsdebugger.debugger;

import android.content.Context;


public class TopAdsLogger {

    private static TopAdsLoggerInterface instance;

    public static TopAdsLoggerInterface getInstance(Context context) {
        if (instance == null) {
            instance = new TopAdsLoggerInterface() {
                @Override
                public boolean isNotificationEnabled() {
                    return false;
                }

                @Override
                public void save(String url, String eventType, String sourceName, String productId,
                                 String productName, String imageUrl, String componentName) {

                }

                @Override
                public void openActivity() {

                }

                @Override
                public void enableNotification(boolean status) {

                }
            };
        }

        return instance;
    }

}
