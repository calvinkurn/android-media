package com.tokopedia.core.router.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * @author Kulomady on 11/22/16.
 */

public class HotListRouter {

    private static final String FRAGMENT_HOTLIST = "com.tokopedia.core.home.fragment.FragmentHotListV2";

    public static String TAG_HOTLIST_FRAGMENT = "FragmentHotListV2";

    public static Fragment getHotListFragment(Context context) {
        Fragment fragment = Fragment.instantiate(context, FRAGMENT_HOTLIST);
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
}
