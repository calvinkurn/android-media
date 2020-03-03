package com.tokopedia.tkpd.qrscanner;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.app.MainApplication;

public class QRTracking extends UnifyTracking {

    public static void eventQRButtonClick() {
        sendGTMEvent(MainApplication.getAppContext(), new EventTracking(
                QREventTracking.Event.GenericCampaignHomeClickEvent,
                QREventTracking.Category.EventTriggerHomeCategory,
                QREventTracking.Action.EventClickTopNav,
                QREventTracking.Label.LabelQRCodeIcon
        ).getEvent());
    }

    public static void eventScanQRCode(String status,String campaignId,String url) {
        sendGTMEvent(MainApplication.getAppContext(), new EventTracking(
                QREventTracking.Event.GenericCampaignEvent,
                QREventTracking.Category.EventTriggerBasedCampaign,
                String.format(QREventTracking.Action.EventScanQRCode,status),
                String.format(QREventTracking.Label.LabelQRCodeScan, campaignId,url)
        ).getEvent());
    }
}
