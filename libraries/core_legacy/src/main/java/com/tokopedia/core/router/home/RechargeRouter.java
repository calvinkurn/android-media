package com.tokopedia.core.router.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * @author Kulomady on 11/22/16.
 */

public class RechargeRouter {


    private static final String FRAGMENT_RECHARGE
            = "com.tokopedia.tkpd.home.recharge.fragment.RechargeFragment";

    private static final String FRAGMENT_RECHARGE_CATEGORY =
            "com.tokopedia.tkpd.home.recharge.fragment.RechargeCategoryFragment";

    private static final String ARG_PARAM_CATEGORY = "ARG_PARAM_CATEGORY";
    public static final String ARG_UTM_SOURCE = "ARG_UTM_SOURCE";
    public static final String ARG_UTM_MEDIUM = "ARG_UTM_MEDIUM";
    public static final String ARG_UTM_CAMPAIGN = "ARG_UTM_CAMPAIGN";
    public static final String ARG_UTM_CONTENT = "ARG_UTM_CONTENT";
    public static final String EXTRA_ALLOW_ERROR = "extra_allow_error";


    public static Fragment getRechargeFragment(Context context) {
        Fragment fragment = Fragment.instantiate(context, FRAGMENT_RECHARGE);
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment getRechargeCategoryFragment(Context context) {
        Fragment fragment = Fragment.instantiate(context, FRAGMENT_RECHARGE_CATEGORY);
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


}
