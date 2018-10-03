package com.tokopedia.core.analytics.handler;

import android.content.Context;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.data.DiskAnalyticsDataStore;

/**
 * @author by alvarisi on 12/28/16.
 */

public class UserAuthenticationAnalytics {
    private static DiskAnalyticsDataStore mDiskAnalyticsDataStore;

    public UserAuthenticationAnalytics(Context context) {
        mDiskAnalyticsDataStore = new DiskAnalyticsDataStore(context);
    }

    private static void checkNotNullAnalyticsData(Context context) {
        if (mDiskAnalyticsDataStore == null) {
            mDiskAnalyticsDataStore = new DiskAnalyticsDataStore(context);
        }
    }

    public static void setActiveLogin(Context context) {
        checkNotNullAnalyticsData(context);
        mDiskAnalyticsDataStore.setActiveAuthenticationState(AppEventTracking.GTMCacheValue.LOGIN);
    }

    public static void setActiveRegister(Context context) {
        checkNotNullAnalyticsData(context);
        mDiskAnalyticsDataStore.setActiveAuthenticationState(AppEventTracking.GTMCacheValue.REGISTER);
    }

    public static void setActiveAuthenticationMedium(Context context, String medium) {
        checkNotNullAnalyticsData(context);
        mDiskAnalyticsDataStore.setActiveAuthenticationMedium(medium);
    }

    public static void sendAnalytics(Context context) {
        checkNotNullAnalyticsData(context);
        switch (mDiskAnalyticsDataStore.getActiveAuthenticationState()) {
            case AppEventTracking.GTMCacheValue.LOGIN:
                UnifyTracking.eventLoginSuccess(context, mDiskAnalyticsDataStore.getActiveAuthenticationMedium());
                CommonUtils.dumper(mDiskAnalyticsDataStore.getActiveAuthenticationMedium());
                break;
            case AppEventTracking.GTMCacheValue.REGISTER:
                UnifyTracking.eventRegisterSuccess(context, mDiskAnalyticsDataStore.getActiveAuthenticationMedium());
                CommonUtils.dumper(mDiskAnalyticsDataStore.getActiveAuthenticationMedium());
                break;
        }
    }
}
