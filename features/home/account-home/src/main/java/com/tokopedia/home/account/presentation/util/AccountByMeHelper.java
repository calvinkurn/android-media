package com.tokopedia.home.account.presentation.util;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;

/**
 * @author by milhamj on 17/01/19.
 */
public class AccountByMeHelper {
    private static final String KEY_PROFILE_BUYER = "KEY_PROFILE_BUYER";
    private static final String KEY_AFFILIATE_FIRSTTIME = "KEY_AFFILIATE_FIRSTTIME";
    private static final String KEY_REKENING_PREMIUM_CLICKED = "key_rekening_premium_clicked";

    public static boolean isFirstTimeByme(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, KEY_PROFILE_BUYER);
        return cache.getBoolean(KEY_AFFILIATE_FIRSTTIME, true);
    }

    public static void setFirstTimeByme(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, KEY_PROFILE_BUYER);
        cache.putBoolean(KEY_AFFILIATE_FIRSTTIME, false);
        cache.applyEditor();
    }


    public static void setRekeningPremiumWidgetClicked(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, KEY_PROFILE_BUYER);
        cache.putBoolean(KEY_REKENING_PREMIUM_CLICKED, true);
        cache.applyEditor();
    }

    public static boolean isRekeningPremiumWidgetClicked(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, KEY_PROFILE_BUYER);
        return cache.getBoolean(KEY_REKENING_PREMIUM_CLICKED, false);
    }

}
