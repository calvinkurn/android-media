package com.tokopedia.core.router.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.gcm.utils.RouterUtils;


/**
 * @author Kulomady on 11/21/16.
 */

public class SimpleHomeRouter {

    public static final String FRAGMENT_TYPE = "FRAGMENT_TYPE";
    public static final int INVALID_FRAGMENT = 0;
    public static final int WISHLIST_FRAGMENT = 1;
    public static final int PRODUCT_HISTORY_FRAGMENT = 2;

    private static final String ACTIVITY_SIMPLE_HOME = "com.tokopedia.tkpd.home.SimpleHomeActivity";


    public static Intent getSimpleHomeActivityIntent(Context context, int wishlistFragment) {
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_TYPE, wishlistFragment);
        Intent intent = RouterUtils.getActivityIntent(context, ACTIVITY_SIMPLE_HOME);
        intent.putExtras(bundle);
        return intent;
    }

    public static Class<?> getSimpleHomeActivityClass() {
        try {
            return RouterUtils.getActivityClass(ACTIVITY_SIMPLE_HOME);
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
            return null;
        }
    }
}
