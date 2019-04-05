package com.tokopedia.tkpd.deeplink.presenter;

import android.app.Activity;
import android.net.Uri;

/**
 * Created by Angga.Prasetiyo on 14/12/2015.
 */
public interface DeepLinkPresenter extends DeepLinkAnalyticsPresenter {

    void processDeepLinkAction(Activity activity, Uri uri);

    boolean isLandingPageWebView(Uri uri);

    void checkUriLogin(Uri uriData);

    void actionGotUrlFromApplink(Uri uriData);

    void mapUrlToApplink(Uri uri);

    void sendAuthenticatedEvent(Uri uriData, String screenName);
}
