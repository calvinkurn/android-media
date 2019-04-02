package com.tokopedia.affiliate.util;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;

/**
 * @author by yfsx on 25/01/19.
 */
public class AffiliateHelper {
    private static final String KEY_AFFILIATE = "KEY_AFFILIATE";
    private static final String KEY_AFFILIATE_EXPLORE_OPEN_PROFILE_FIRSTTIME = "KEY_AFFILIATE_EXPLORE_OPEN_PROFILE_FIRSTTIME";

    public static boolean isFirstTimeOpenProfileFromExplore(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context.getApplicationContext(), KEY_AFFILIATE);
        return cache.getBoolean(KEY_AFFILIATE_EXPLORE_OPEN_PROFILE_FIRSTTIME, true);
    }

    public static void setFirstTimeOpenProfileFromExplore(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context.getApplicationContext(), KEY_AFFILIATE);
        cache.putBoolean(KEY_AFFILIATE_EXPLORE_OPEN_PROFILE_FIRSTTIME, false);
        cache.applyEditor();
    }
}
