package com.tokopedia.core.analytics.screen;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.ScreenTrackingBuilder;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.database.manager.GlobalCacheManager;

import java.util.concurrent.TimeUnit;

/**
 * @author okasurya on 3/3/18.
 */

public class IndexScreenTracking extends TrackingUtils {
    private static final String COMP_1 = "com.gojek.app";
    private static final String COMP_2 = "com.shopee.id";
    private static final String COMP_3 = "com.lazada.android";
    private static final String COMP_4 = "com.bukalapak.android";
    private static final String COMP_5 = "com.grabtaxi.passenger";
    private static final String COMP_6 = "com.traveloka.android";
    private static final String CI_DATA = "ci_data";
    private static final long EXPIRED_TIME = TimeUnit.DAYS.toSeconds(30);

    public static void sendScreen(Activity activity,
                                  ScreenTracking.IOpenScreenAnalytics openScreenAnalytics) {
        try {
            ScreenTrackingBuilder
                    .newInstance(activity, openScreenAnalytics, getAfUniqueId())
                    .setNetworkSpeed()
                    .setKeyCompetitorIntelligence(getCIData(activity))
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getCIData(Context context) {
        GlobalCacheManager cache = new GlobalCacheManager();
        if(cache.get(CI_DATA) != null
                && !cache.isExpired(CI_DATA)
                && !cache.get(CI_DATA).isEmpty()) {
            return cache.get(CI_DATA);
        } else {
            String value = getCurrentInstalledList(context);
            cache.setKey(CI_DATA);
            cache.setCacheDuration(EXPIRED_TIME);
            cache.setValue(value);
            cache.store();

            return value;
        }
    }

    private static String getCurrentInstalledList(Context context) {
        String[] competitions = {
                COMP_1, COMP_2, COMP_3, COMP_4, COMP_5, COMP_6
        };
        StringBuilder compList = new StringBuilder();
        PackageManager pm = context.getPackageManager();
        for (int i = 0; i < competitions.length; i++) {
            if (pm != null && isAppInstalled(competitions[i], pm)) {
                compList.append("app ").append(i + 1).append("-");
            }
        }
        return compList.toString().substring(0, compList.length()-1);
    }

    private static boolean isAppInstalled(String uri, PackageManager pm) {
        try {
            pm.getPackageInfo(uri, PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }
}
