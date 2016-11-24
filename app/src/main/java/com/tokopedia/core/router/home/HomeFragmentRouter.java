package com.tokopedia.core.router.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * @author Kulomady on 11/22/16.
 */

public class HomeFragmentRouter {

    private static final java.lang.String FRAGMENT_HOME
            = "com.tokopedia.tkpd.home.fragment.FragmentIndexCategory";


    public static Fragment getHomeFragment(Context context) {
        Fragment fragment = Fragment.instantiate(context, FRAGMENT_HOME);
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
}
