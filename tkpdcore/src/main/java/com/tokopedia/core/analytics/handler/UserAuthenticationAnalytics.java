package com.tokopedia.core.analytics.handler;

import android.os.Bundle;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.data.DiskAnalyticsDataStore;

/**
 * @author by alvarisi on 12/28/16.
 */

public class UserAuthenticationAnalytics {
    private static DiskAnalyticsDataStore mDiskAnalyticsDataStore;

    public UserAuthenticationAnalytics() {
        mDiskAnalyticsDataStore = new DiskAnalyticsDataStore();
    }

    private static void checkNotNullAnalyticsData() {
        if (mDiskAnalyticsDataStore == null) {
            mDiskAnalyticsDataStore = new DiskAnalyticsDataStore();
        }
    }

    public static void setActiveLogin() {
        checkNotNullAnalyticsData();
        mDiskAnalyticsDataStore.setActiveAuthenticationState(AppEventTracking.GTMCacheValue.LOGIN);
    }

    public static void setActiveRegister() {
        checkNotNullAnalyticsData();
        mDiskAnalyticsDataStore.setActiveAuthenticationState(AppEventTracking.GTMCacheValue.REGISTER);
    }

    public static void setActiveAuthenticationMedium(String medium) {
        checkNotNullAnalyticsData();
        mDiskAnalyticsDataStore.setActiveAuthenticationMedium(medium);
    }
}
