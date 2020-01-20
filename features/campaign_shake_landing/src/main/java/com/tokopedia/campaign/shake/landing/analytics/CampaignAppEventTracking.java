package com.tokopedia.campaign.shake.landing.analytics;

import com.tokopedia.core.analytics.AppEventTracking;

import static com.tokopedia.core.analytics.AppEventTracking.Event.USER_INTERACTION_HOMEPAGE;

/**
 * Created by sandeepgoyal on 03/01/18.
 */

public class CampaignAppEventTracking implements AppEventTracking {
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
