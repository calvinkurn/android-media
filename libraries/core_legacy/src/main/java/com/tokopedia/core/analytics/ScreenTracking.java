package com.tokopedia.core.analytics;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.track.TrackApp;

import java.util.Map;

/**
 * @author by Herdi_WORK on 25.10.16.
 * modified by Alvarisi
 * This Class for screen tracking
 */

public class ScreenTracking extends TrackingUtils {

    public interface IOpenScreenAnalytics {
        String getScreenName();
    }

    public static void sendScreen(Activity activity, IOpenScreenAnalytics openScreenAnalytics) {
        try {
            ScreenTrackingBuilder
                    .newInstance(openScreenAnalytics)
                    .execute(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendScreen(Context context, String screenName) {
        try {
            ScreenTrackingBuilder
                    .newInstance(screenName)
                    .execute(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void screen(Context context, String screen) {
        if (TextUtils.isEmpty(screen)) {
            return;
        }
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(screen);
    }

    static void eventAuthScreen(Context context, Map<String, String> customDimension, String screenName) {
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(screenName, customDimension);
    }
}