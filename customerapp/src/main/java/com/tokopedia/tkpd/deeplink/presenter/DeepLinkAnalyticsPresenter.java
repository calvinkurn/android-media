package com.tokopedia.tkpd.deeplink.presenter;

/**
 * Created by Herdi_WORK on 30.05.17.
 */

public interface DeepLinkAnalyticsPresenter {

    void processAFlistener();

    void sendCampaignGTM(String campaignUri, String screenName);

}
