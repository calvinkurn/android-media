package com.tokopedia.transactionanalytics;

import com.google.android.gms.tagmanager.DataLayer;

import java.util.HashMap;

/**
 * Created by Irfan Khoirul on 07/02/19.
 */

public class ExpressCheckoutAnalyticsTracker extends TransactionAnalytics {

    public ExpressCheckoutAnalyticsTracker() {
    }

    public void enhanceEcommerceImpressionExpressCheckoutForm(HashMap<String, Object> data, String eventLabel) {
        sendEnhancedEcommerce(
                DataLayer.mapOf(
                        ConstantTransactionAnalytics.Key.EVENT, ConstantTransactionAnalytics.EventName.CHECKOUT,
                        ConstantTransactionAnalytics.Key.EVENT_CATEGORY, ConstantTransactionAnalytics.EventCategory.EXPRESS_CHECKOUT,
                        ConstantTransactionAnalytics.Key.EVENT_ACTION, ConstantTransactionAnalytics.EventAction.VIEW_EXPRESS_CHECKOUT,
                        ConstantTransactionAnalytics.Key.EVENT_LABEL, eventLabel,
                        ConstantTransactionAnalytics.Key.E_COMMERCE, data)
        );
    }

    public void eventClickBuyAndError(String errorMessage) {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.CLICK_CHECKOUT_EXPRESS,
                ConstantTransactionAnalytics.EventCategory.EXPRESS_CHECKOUT,
                ConstantTransactionAnalytics.EventAction.CLICK_BAYAR,
                "not success - " + errorMessage
        );
    }

    public void eventClickContinueWithoutTemplate(boolean isDefault) {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.CLICK_CHECKOUT_EXPRESS,
                ConstantTransactionAnalytics.EventCategory.EXPRESS_CHECKOUT,
                ConstantTransactionAnalytics.EventAction.CLICK_LANJUTKAN_TANPA_TEMPLATE,
                isDefault ? ConstantTransactionAnalytics.EventLabel.SUCCESS_DEFAULT : ConstantTransactionAnalytics.EventLabel.SUCCESS_NOT_DEFAULT
        );
    }

    public void eventClickButtonX() {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.CLICK_CHECKOUT_EXPRESS,
                ConstantTransactionAnalytics.EventCategory.EXPRESS_CHECKOUT,
                ConstantTransactionAnalytics.EventAction.CLICK_X,
                ""
        );
    }

    public void viewErrorMetodePembayaran() {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.VIEW_CHECKOUT_EXPRESS,
                ConstantTransactionAnalytics.EventCategory.EXPRESS_CHECKOUT,
                ConstantTransactionAnalytics.EventAction.VIEW_ERROR_METODE_PEMBAYARAN,
                ""
        );
    }

    public void clickPilihMetodePembayaran(String gateway) {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.CLICK_CHECKOUT_EXPRESS,
                ConstantTransactionAnalytics.EventCategory.EXPRESS_CHECKOUT,
                ConstantTransactionAnalytics.EventAction.CLICK_PILIH_METODE_PEMBAYARAN,
                gateway
        );
    }

}
