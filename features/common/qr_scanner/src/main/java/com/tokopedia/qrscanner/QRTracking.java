package com.tokopedia.qrscanner;

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

public class QRTracking {

    public static void eventQRButtonClick() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                QREventTracking.Event.GenericCampaignHomeClickEvent,
                QREventTracking.Category.EventTriggerHomeCategory,
                QREventTracking.Action.EventClickTopNav,
                QREventTracking.Label.LabelQRCodeIcon
        ));
    }

    public static void eventScanQRCode(String status,String campaignId,String url) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                QREventTracking.Event.GenericCampaignEvent,
                QREventTracking.Category.EventTriggerBasedCampaign,
                String.format(QREventTracking.Action.EventScanQRCode,status),
                String.format(QREventTracking.Label.LabelQRCodeScan, campaignId,url)
        ));
    }
}
