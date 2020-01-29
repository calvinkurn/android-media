package com.tokopedia.campaign.shake.landing.analytics;

/**
 * Created by sandeepgoyal on 03/01/18.
 */

public class CampaignAppEventTracking {
    interface Event {
        String GenericCampaignEvent = "campaignEvent";

    }

    interface Category {
        String EventTriggerBasedCampaign =
                "trigger based campaign";

    }

    interface Action {
        String EventShakeDevice = "%s - shake device - %s";

    }

    interface Label {
        String LabelShake = "%s - %s";
    }
}
