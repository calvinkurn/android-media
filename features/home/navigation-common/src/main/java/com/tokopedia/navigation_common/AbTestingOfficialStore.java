package com.tokopedia.navigation_common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by meta on 18/01/19.
 */
public class AbTestingOfficialStore {

    private static final String KEY_OS_AB_TEST = "os_ab_test";

    private SharedPreferences cacheManager;

    public AbTestingOfficialStore(Context context) {
        if (context == null)
            return;
        cacheManager = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean shouldDoAbTesting() {
        if (cacheManager != null) {
            return cacheManager.getBoolean(KEY_OS_AB_TEST, false);
        }
        return false;
    }

    public void putCacheForAbTesting(boolean isOsBottomNavShow) {
        if (cacheManager != null) {
            cacheManager.edit()
                    .putBoolean(KEY_OS_AB_TEST, isOsBottomNavShow)
                    .apply();
        }
    }
}
