package com.tokopedia.tkpd.deeplink.listener;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Angga.Prasetiyo on 14/12/2015.
 */
public interface DeepLinkView {

    void inflateFragment(Fragment fragment, String tag);

    void initDeepLink();

    void networkError(Uri uriData);

    void goToPage(Intent destination);
}
