package com.tokopedia.sellerapp.deeplink.presenter;

/**
 * @author rizkyfadillah on 27/07/2017.
 */

public interface DeepLinkAnalyticsPresenter {

    void processAFlistener();

    void sendCampaignGTM(String campaignUri, String screenName);

}
