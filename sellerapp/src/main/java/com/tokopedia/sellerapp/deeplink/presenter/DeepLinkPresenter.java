package com.tokopedia.sellerapp.deeplink.presenter;

import android.app.Activity;
import android.net.Uri;

/**
 * Created by Herdi_WORK on 10.05.17.
 */

public interface DeepLinkPresenter {

    void processDeepLinkAction(Activity activity, Uri uri);

    void sendCampaignGTM(Activity activity, String campaignUri, String screenName);
}
