package com.tokopedia.sellerapp.deeplink.listener;

import android.app.Fragment;

/**
 * Created by Herdi_WORK on 16.05.17.
 */

public interface DeepLinkView {


    void inflateFragment(Fragment fragment, String tag);

    void inflateFragmentV4(android.support.v4.app.Fragment fragment, String tag);

    void replaceFragment(Fragment fragment, String tag);

    void hideActionBar();

}
