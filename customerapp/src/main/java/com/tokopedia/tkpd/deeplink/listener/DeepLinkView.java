package com.tokopedia.tkpd.deeplink.listener;

import android.content.Intent;
import android.net.Uri;

/**
 * Created by Angga.Prasetiyo on 14/12/2015.
 */
public interface DeepLinkView {

    void initDeepLink();

    void networkError(Uri uriData);

    void goToPage(Intent destination);
}
