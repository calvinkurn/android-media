package com.tokopedia.core.router;

/**
 * @author  by alvarisi on 11/24/16.
 */

public class CustomerRouter {

    public static final String IS_DEEP_LINK_SEARCH = "IS_DEEP_LINK_SEARCH";

    private static final String DEEPLINK_ACTIVITY = "com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity";

    public static Class<?> getDeeplinkClass(){
        try {
            return Class.forName(DEEPLINK_ACTIVITY);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}