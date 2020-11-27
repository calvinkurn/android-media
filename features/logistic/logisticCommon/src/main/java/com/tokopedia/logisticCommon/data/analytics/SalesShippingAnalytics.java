package com.tokopedia.logisticCommon.data.analytics;

import javax.inject.Inject;

public class SalesShippingAnalytics extends LogisticAnalytics {
    @Inject
    public SalesShippingAnalytics() {

    }

    public void eventClickShippingSalesShippingClickTombolScanAwb() {
        sendEventCategoryAction(ConstantLogisticAnalytics.EventName.CLICK_SHIPPING,
                ConstantLogisticAnalytics.EventCategory.SALES_SHIPPING,
                ConstantLogisticAnalytics.EventAction.CLICK_TOMBOL_SCAN_AWB);
    }

    public void eventViewShippingSalesShippingViewScanAwbSuccess() {
        sendEventCategoryActionLabel(ConstantLogisticAnalytics.EventName.VIEW_SHIPPING,
                ConstantLogisticAnalytics.EventCategory.SALES_SHIPPING,
                ConstantLogisticAnalytics.EventAction.VIEW_SCAN_AWB,
                ConstantLogisticAnalytics.EventLabel.SUCCESS
        );
    }

    public void eventClickShippingSalesShippingClickTombolSelesaiSuccess() {
        sendEventCategoryActionLabel(ConstantLogisticAnalytics.EventName.CLICK_SHIPPING,
                ConstantLogisticAnalytics.EventCategory.SALES_SHIPPING,
                ConstantLogisticAnalytics.EventAction.CLICK_TOMBOL_SELESAI,
                ConstantLogisticAnalytics.EventLabel.SUCCESS
        );
    }

    public void eventClickShippingSalesShippingClickTombolSelesaiFail() {
        sendEventCategoryActionLabel(ConstantLogisticAnalytics.EventName.CLICK_SHIPPING,
                ConstantLogisticAnalytics.EventCategory.SALES_SHIPPING,
                ConstantLogisticAnalytics.EventAction.CLICK_TOMBOL_SELESAI,
                ConstantLogisticAnalytics.EventLabel.FAILED
        );
    }

    public void eventClickShippingSalesShippingClickExitScanAwb() {
        sendEventCategoryAction(ConstantLogisticAnalytics.EventName.CLICK_SHIPPING,
                ConstantLogisticAnalytics.EventCategory.SALES_SHIPPING,
                ConstantLogisticAnalytics.EventAction.EXIT_SCAN_AWB);
    }

    public void eventViewShippingSalesShippingImpressionScanAwbPage() {
        sendEventCategoryAction(ConstantLogisticAnalytics.EventName.VIEW_SHIPPING,
                ConstantLogisticAnalytics.EventCategory.SALES_SHIPPING,
                ConstantLogisticAnalytics.EventAction.IMPRESSION_SCAN_AWB_PAGE);
    }
}
