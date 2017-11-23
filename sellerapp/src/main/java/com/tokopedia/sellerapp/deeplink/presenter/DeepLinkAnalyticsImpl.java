package com.tokopedia.sellerapp.deeplink.presenter;

import android.net.Uri;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.deeplink.DeeplinkUTMUtils;
import com.tokopedia.core.analytics.nishikino.model.Campaign;

/**
 * @author rizkyfadillah on 27/07/2017.
 */

public class DeepLinkAnalyticsImpl implements DeepLinkAnalyticsPresenter {

    @Override
    public void processAFlistener() {

    }

    @Override
    public void sendCampaignGTM(String campaignUri, String screenName) {
        Campaign campaign = DeeplinkUTMUtils.convertUrlCampaign(Uri.parse(campaignUri));
        campaign.setScreenName(screenName);
        UnifyTracking.eventCampaign(campaign);
        UnifyTracking.eventCampaign(campaignUri);
    }

    public void processUTM(Uri applink){
        if(DeeplinkUTMUtils.isValidCampaignUrl(applink)){
            sendCampaignGTM(applink.toString(), AppScreen.SCREEN_DEEPLINK_APPLINKHANDLER);
        }
    }

}
