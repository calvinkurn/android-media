package com.tokopedia.logisticanalytics;

public interface ConstantLogisticAnalytics {

    interface Key {
        String EVENT = "event";
        String EVENT_CATEGORY = "eventCategory";
        String EVENT_ACTION = "eventAction";
        String EVENT_LABEL = "eventLabel";
        String E_COMMERCE = "ecommerce";
    }

    interface EventName {
        String CLICK_SHIPPING = "clickShipping";
        String VIEW_SHIPPING = "viewShipping";
    }

    interface EventCategory {
        String SALES_SHIPPING = "sales shipping";
    }

    interface EventAction {
        String CLICK_TOMBOL_SCAN_AWB = "click tombol scan awb";
        String VIEW_SCAN_AWB = "view scan awb";
        String CLICK_TOMBOL_SELESAI = "click tombol selesai";
        String EXIT_SCAN_AWB = "exit scan awb";
        String IMPRESSION_SCAN_AWB_PAGE = "impression scan awb page";
    }

    interface EventLabel {
        String SUCCESS = "success";
        String FAILED = "fail";
    }

    interface ScreenName {

    }
}
