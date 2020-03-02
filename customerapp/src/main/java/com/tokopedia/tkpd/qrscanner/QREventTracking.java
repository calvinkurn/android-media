package com.tokopedia.tkpd.qrscanner;

import com.tokopedia.core.analytics.AppEventTracking;

/**
 * Created by sandeepgoyal on 03/01/18.
 */

public class QREventTracking implements AppEventTracking {
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
        String EventScanQRCode = "scan qr code -  %s";
        String EventClickTopNav = "click top nav";

    }

    interface Label {
        String LabelQRCodeScan = "%s - %s";
        String LabelQRCodeIcon = "qr code icon";

    }
}
