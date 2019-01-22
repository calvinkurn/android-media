package com.tokopedia.tkpd.campaign.analytics;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.app.MainApplication;

/**
 * Created by sandeepgoyal on 03/01/18.
 */

public class CampaignTracking extends UnifyTracking {

    public static void eventShakeShake(String status,String screenName,String campaignId,String url) {
        sendGTMEvent(MainApplication.getAppContext(), new EventTracking(
                CampaignAppEventTracking.Event.GenericCampaignEvent,
                CampaignAppEventTracking.Category.EventTriggerBasedCampaign,
                String.format(CampaignAppEventTracking.Action.EventShakeDevice,screenName,status),
                String.format(CampaignAppEventTracking.Label.LabelShake, campaignId,url)
        ).getEvent());
    }

    public static void eventQRButtonClick() {
        sendGTMEvent(MainApplication.getAppContext(), new EventTracking(
                CampaignAppEventTracking.Event.GenericCampaignHomeClickEvent,
                CampaignAppEventTracking.Category.EventTriggerHomeCategory,
                CampaignAppEventTracking.Action.EventClickTopNav,
                CampaignAppEventTracking.Label.LabelQRCodeIcon
        ).getEvent());
    }

    public static void eventScanQRCode(String status,String campaignId,String url) {
        sendGTMEvent(MainApplication.getAppContext(), new EventTracking(
                CampaignAppEventTracking.Event.GenericCampaignEvent,
                CampaignAppEventTracking.Category.EventTriggerBasedCampaign,
                String.format(CampaignAppEventTracking.Action.EventScanQRCode,status),
                String.format(CampaignAppEventTracking.Label.LabelQRCodeScan, campaignId,url)
        ).getEvent());
    }
}
