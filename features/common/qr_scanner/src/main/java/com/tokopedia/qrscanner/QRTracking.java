package com.tokopedia.qrscanner;

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import java.util.Map;

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

    public static void eventScanQRCode(String userId) {
        Map<String, Object> map = TrackAppUtils.gtmData(
                QREventTracking.Event.GenericClickLoginEvent,
                QREventTracking.Category.EventLoginWithQrCodeCategory,
                QREventTracking.Action.EventScanningQrCode,
                ""
        );
        map.put(QREventTracking.BusinessUnit.BusinessUnit, QREventTracking.BusinessUnit.UserPlatformUnit);
        map.put(QREventTracking.CurrentSite.CurrentSite, QREventTracking.CurrentSite.TokopediaMarketplaceSite);
        map.put(QREventTracking.UserId.UserId, userId);
        TrackApp.getInstance().getGTM().sendGeneralEvent(map);
    }
}
