package com.tokopedia.qrscanner;

/**
 * Created by sandeepgoyal on 03/01/18.
 */

public class QREventTracking {
    interface Event {
        String GenericCampaignEvent = "campaignEvent";
        String GenericCampaignHomeClickEvent =  "clickHomePage";
        String GenericClickLoginEvent =  "clickLogin";
    }

    interface Category {
        String EventTriggerBasedCampaign =
                "trigger based campaign";
        String EventTriggerHomeCategory = "homepage";
        String EventLoginWithQrCodeCategory = "login with qr code";
    }

    interface Action {
        String EventScanQRCode = "scan qr code -  %s";
        String EventClickTopNav = "click top nav";
        String EventScanningQrCode = "scanning qr code";
    }

    interface Label {
        String LabelQRCodeScan = "%s - %s";
        String LabelQRCodeIcon = "qr code icon";
    }

    interface BusinessUnit {
        String BusinessUnit = "businessUnit";
        String UserPlatformUnit = "user platform";
    }

    interface CurrentSite {
        String CurrentSite = "currentSite";
        String TokopediaMarketplaceSite = "tokopediamarketplace";
    }

    interface UserId {
        String UserId = "userId";
    }
}
