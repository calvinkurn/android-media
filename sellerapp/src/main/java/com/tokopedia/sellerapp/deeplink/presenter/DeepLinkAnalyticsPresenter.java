package com.tokopedia.sellerapp.deeplink.presenter;

import android.app.Activity;

/**
 * @author rizkyfadillah on 27/07/2017.
 */

public interface DeepLinkAnalyticsPresenter {

    void processAFlistener();

    void sendCampaignGTM(Activity activity, String campaignUri, String screenName);

}
