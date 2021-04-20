package com.tokopedia.tkpd.deeplink.presenter;

import android.app.Activity;
import android.net.Uri;

import com.tokopedia.core.analytics.nishikino.model.Campaign;

/**
 * Created by Angga.Prasetiyo on 14/12/2015.
 */
public interface DeepLinkPresenter extends DeepLinkAnalyticsPresenter {

    void processDeepLinkAction(Activity activity, Uri uri);

    void checkUriLogin(Uri uriData);

    void actionGotUrlFromApplink(Uri uriData);

    void sendAuthenticatedEvent(Uri uriData, String screenName);

    void sendAuthenticatedEvent(Uri uriData, Campaign campaign, String screenName);
}
