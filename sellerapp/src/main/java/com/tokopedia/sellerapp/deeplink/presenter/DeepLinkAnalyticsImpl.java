package com.tokopedia.sellerapp.deeplink.presenter;

import android.app.Activity;
import android.net.Uri;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.track.TrackApp;

/**
 * @author rizkyfadillah on 27/07/2017.
 */

public class DeepLinkAnalyticsImpl implements DeepLinkAnalyticsPresenter {

    @Override
    public void sendCampaignGTM(Activity activity, String campaignUri, String screenName) {
        TrackApp.getInstance().getGTM().sendCampaign(activity, campaignUri, screenName, false);
    }

    public void processUTM(Activity activity, Uri applink){
        sendCampaignGTM(activity, applink.toString(), AppScreen.SCREEN_DEEPLINK_APPLINKHANDLER);
    }

}
