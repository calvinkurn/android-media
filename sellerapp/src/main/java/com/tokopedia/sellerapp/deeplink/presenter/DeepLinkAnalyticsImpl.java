package com.tokopedia.sellerapp.deeplink.presenter;

import android.app.Activity;
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
    public void sendCampaignGTM(Activity activity, String campaignUri, String screenName) {
        Campaign campaign = DeeplinkUTMUtils.convertUrlCampaign(Uri.parse(campaignUri));
        campaign.setScreenName(screenName);
        UnifyTracking.eventCampaign(activity, campaign);
        UnifyTracking.eventCampaign(activity, campaignUri);
    }

    public void processUTM(Activity activity, Uri applink){
        if(DeeplinkUTMUtils.isValidCampaignUrl(applink)){
            sendCampaignGTM(activity, applink.toString(), AppScreen.SCREEN_DEEPLINK_APPLINKHANDLER);
        }
    }

}
