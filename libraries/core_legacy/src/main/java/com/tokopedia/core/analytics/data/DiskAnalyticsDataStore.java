package com.tokopedia.core.analytics.data;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.app.MainApplication;

/**
 * @author by alvarisi on 1/4/17.
 */

public class DiskAnalyticsDataStore {
    private LocalCacheHandler mLocalCacheHandler;

    public DiskAnalyticsDataStore() {
        mLocalCacheHandler = new LocalCacheHandler(
                MainApplication.getAppContext(),
                AppEventTracking.GTM_CACHE
        );
    }

    public void setActiveAuthenticationState(String state) {
        mLocalCacheHandler.putString(AppEventTracking.GTMCacheKey.SESSION_STATE, state);
        mLocalCacheHandler.applyEditor();
    }

    public String getActiveAuthenticationState() {
        return mLocalCacheHandler.getString(AppEventTracking.GTMCacheKey.SESSION_STATE, "");
    }

    public void setActiveAuthenticationMedium(String medium) {
        mLocalCacheHandler.putString(AppEventTracking.GTMCacheKey.MEDIUM, medium);
        mLocalCacheHandler.applyEditor();
    }

    public String getActiveAuthenticationMedium() {
        return mLocalCacheHandler.getString(AppEventTracking.GTMCacheKey.MEDIUM,
                AppEventTracking.DEFAULT_CHANNEL);
    }
}
