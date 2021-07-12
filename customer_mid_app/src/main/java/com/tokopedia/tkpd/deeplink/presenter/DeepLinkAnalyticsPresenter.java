package com.tokopedia.tkpd.deeplink.presenter;

import android.app.Activity;

/**
 * Created by Herdi_WORK on 30.05.17.
 */

public interface DeepLinkAnalyticsPresenter {

    void processAFlistener();

    void sendCampaignGTM(Activity activity, String campaignUri, String screenName, boolean isAmp);

}
