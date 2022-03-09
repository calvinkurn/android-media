package com.tokopedia.analyticsdebugger.cassava;

import android.content.Context;

import com.tokopedia.analyticsdebugger.cassava.AnalyticsLogger;
import com.tokopedia.analyticsdebugger.cassava.AnalyticsSource;

import java.util.Map;

/**
 * @author okasurya on 5/16/18.
 */
public class GtmLogger {

    private static AnalyticsLogger instance;

    public static AnalyticsLogger getInstance(Context context) {
        if (instance == null) {
            instance = new AnalyticsLogger() {
                @Override
                public void save(Map<String, Object> data, String name,  @AnalyticsSource String source) {

                }

                @Override
                public void enableNotification(boolean status) {

                }

                @Override
                public Boolean isNotificationEnabled() {
                    return false;
                }
            };
        }

        return instance;
    }

}
