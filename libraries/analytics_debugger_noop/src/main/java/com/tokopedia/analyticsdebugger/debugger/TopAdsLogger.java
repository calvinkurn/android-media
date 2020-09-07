package com.tokopedia.analyticsdebugger.debugger;

import android.content.Context;

import org.jetbrains.annotations.NotNull;


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
                public void save(@NotNull String url, @NotNull String eventType, @NotNull String sourceName,
                        @NotNull String productId, @NotNull String productName, @NotNull String imageUrl, @NotNull String componentName) {

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
