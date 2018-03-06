package com.tokopedia.tkpd.campaign.analytics;

import com.tokopedia.core.analytics.AppEventTracking;

import static com.tokopedia.core.analytics.AppEventTracking.Event.USER_INTERACTION_HOMEPAGE;

/**
 * Created by sandeepgoyal on 03/01/18.
 */

public class CampaignAppEventTracking implements AppEventTracking {
    interface Event {
        String GenericCampaignEvent = "campaignEvent";
        String GenericCampaignHomeClickEvent =  "clickHomePage";

    }

    interface Category {
        String EventTriggerBasedCampaign =
                "trigger based campaign";
        String EventTriggerHomeCategory = "homepage";

    }

    interface Action {
        String EventShakeDevice = "%s - shake device - %s";
        String EventScanQRCode = "scan qr code -  %s";
        String EventClickTopNav = "click top nav";

    }

    interface Label {
        String LabelShake = "%s - %s";
        String LabelQRCodeScan = "%s - %s";
        String LabelQRCodeIcon = "qr code icon";

    }
}
