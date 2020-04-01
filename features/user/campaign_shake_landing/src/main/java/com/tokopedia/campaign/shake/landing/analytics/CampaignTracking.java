package com.tokopedia.campaign.shake.landing.analytics;

import com.tokopedia.track.TrackApp;

/**
 * Created by sandeepgoyal on 03/01/18.
 */

public class CampaignTracking {

    public static void eventShakeShake(String status,String screenName,String campaignId,String url) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                CampaignAppEventTracking.Event.GenericCampaignEvent,
                CampaignAppEventTracking.Category.EventTriggerBasedCampaign,
                String.format(CampaignAppEventTracking.Action.EventShakeDevice,screenName,status),
                String.format(CampaignAppEventTracking.Label.LabelShake, campaignId,url)
        );
    }
}
