package com.tokopedia.core.analytics.screen;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.ScreenTrackingBuilder;
import com.tokopedia.core.analytics.TrackingUtils;

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
    private static final String CI_DATA = "CI_DATA";
    private static final String APP_NAME_PREFIX = "app ";
    private static final String APP_LIST_SEPARATOR = "-";
    private static final long EXPIRED_TIME = TimeUnit.DAYS.toMillis(30);

    public static void sendScreen(Context context,
                                  ScreenTracking.IOpenScreenAnalytics openScreenAnalytics) {
        try {
            ScreenTrackingBuilder
                    .newInstance(openScreenAnalytics)
                    .setNetworkSpeed(getNetworkSpeed(context))
                    .setKeyCompetitorIntelligence(getCIData(context))
                    .execute(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getCIData(Context context) {
        String value = PersistentCacheManager.instance.getString(CI_DATA);
        if (!TextUtils.isEmpty(value)) {
            return value;
        } else {
            value = getCurrentInstalledList(context);
            PersistentCacheManager.instance.put(
                    CI_DATA,
                    value,
                    EXPIRED_TIME
            );
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
                compList.append(APP_NAME_PREFIX).append(i + 1);
                if (i < competitions.length - 1) compList.append(APP_LIST_SEPARATOR);
            }
        }
        return compList.toString();
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
