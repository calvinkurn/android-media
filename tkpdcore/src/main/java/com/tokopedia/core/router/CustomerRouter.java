package com.tokopedia.core.router;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.util.RouterUtils;

/**
 * @author  by alvarisi on 11/24/16.
 */

public class CustomerRouter {

    public static final String IS_DEEP_LINK_SEARCH = "IS_DEEP_LINK_SEARCH";

    private static final String DEEPLINK_ACTIVITY = "com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity";
    private static final String TRUECALLER_ACTIVITY = "com.tokopedia.tkpd.truecaller.TruecallerActivity";

    public static Class<?> getDeeplinkClass(){
        try {
            return Class.forName(DEEPLINK_ACTIVITY);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Intent getTruecallerIntent(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, TRUECALLER_ACTIVITY);
        return intent;
    }
}