package com.tokopedia.transactionanalytics;

import com.google.android.gms.tagmanager.DataLayer;
import com.google.gson.JsonObject;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import java.util.HashMap;

/**
 * Created by Irfan Khoirul on 07/02/19.
 */

public class ExpressCheckoutAnalyticsTracker extends TransactionAnalytics {

    public ExpressCheckoutAnalyticsTracker(AnalyticTracker analyticTracker) {
        super(analyticTracker);
    }

    public void enhanceEcommerceImpressionExpressCheckoutForm(HashMap<String, JsonObject> data, String eventLabel) {
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
}
