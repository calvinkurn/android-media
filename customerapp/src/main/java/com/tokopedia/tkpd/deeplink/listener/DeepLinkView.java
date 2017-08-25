package com.tokopedia.tkpd.deeplink.listener;

import android.app.Fragment;

/**
 * Created by Angga.Prasetiyo on 14/12/2015.
 */
public interface DeepLinkView {

    void inflateFragment(Fragment fragment, String tag);

    void inflateFragmentV4(android.support.v4.app.Fragment fragment, String tag);

    void replaceFragment(Fragment fragment, String tag);

    void hideActionBar();

    void actionChangeToolbarWithBackToNative();
}
