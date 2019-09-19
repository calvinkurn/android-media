package com.tokopedia.sellerapp.deeplink.presenter;

import android.net.Uri;

/**
 * Created by Herdi_WORK on 10.05.17.
 */

public interface DeepLinkPresenter {

    void processDeepLinkAction(Uri uri);

    void sendCampaignGTM(String campaignUri, String screenName);
}
