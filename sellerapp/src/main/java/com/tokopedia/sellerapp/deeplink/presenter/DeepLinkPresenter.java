package com.tokopedia.sellerapp.deeplink.presenter;

import android.net.Uri;

/**
 * Created by Herdi_WORK on 10.05.17.
 */

public interface DeepLinkPresenter {

    void processDeepLinkAction(Uri uri);

    void processAFlistener();

    void sendCampaignGTM(String campaignUri, String screenName);

    boolean isLandingPageWebView(Uri uri);

    void checkUriLogin(Uri uriData);

}
