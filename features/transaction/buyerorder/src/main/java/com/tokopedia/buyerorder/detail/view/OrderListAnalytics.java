package com.tokopedia.buyerorder.detail.view;

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import javax.inject.Inject;

public class OrderListAnalytics {

    private static final String PRODUCT_EVENT_NAME = "clickPurchaseList";
    private static final String PRODUCT_EVENT_DETAIL = "my purchase list detail - mp";
    private static final String EVENT_ACTION_DOWNLOAD_INVOICE = "click button download invoice";

    @Inject
    public OrderListAnalytics() {
    }

    private void sendGtmDataDetails(String eventAction, String eventLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(PRODUCT_EVENT_NAME, PRODUCT_EVENT_DETAIL, eventAction, eventLabel));

    }

    public void sendDownloadEventData(String eventLabel){
        sendGtmDataDetails(EVENT_ACTION_DOWNLOAD_INVOICE, eventLabel);
    }
}
