package com.tokopedia.tkpd.deeplink.presenter;

import android.app.Activity;
import android.net.Uri;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.deeplink.DeeplinkUTMUtils;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.track.TrackApp;


/**
 * Created by Herdi_WORK on 30.05.17.
 */

public class DeepLinkAnalyticsImpl implements DeepLinkAnalyticsPresenter {

    @Override
    public void processAFlistener() {

    }

    @Override
    public void sendCampaignGTM(Activity activity, String campaignUri, String screenName, boolean isAmp) {
        TrackApp.getInstance().getGTM().sendCampaign(activity, campaignUri, screenName, isAmp);
    }

    public void processUTM(Activity activity, Uri applink) {
        sendCampaignGTM(activity, applink.toString(), AppScreen.SCREEN_DEEPLINK_APPLINKHANDLER, false);
    }

}
