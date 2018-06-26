package com.tokopedia.transactionanalytics;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import java.util.Map;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 06/06/18.
 */
public class CheckoutAnalyticsAddToCart extends CheckoutAnalytics {

    @Inject
    public CheckoutAnalyticsAddToCart(AnalyticTracker analyticTracker) {
        super(analyticTracker);
    }

    public void enhancedECommerceAddToCart(Map<String, Object> cartMap, String eventLabel) {
        if (analyticTracker != null)
            analyticTracker.sendEnhancedEcommerce(
                    DataLayer.mapOf("event", ConstantTransactionAnalytics.EventName.ADD_TO_CART,
                            "eventCategory", ConstantTransactionAnalytics.EventCategory.ADD_TO_CART,
                            "eventAction", ConstantTransactionAnalytics.EventAction.CLICK_BELI,
                            "eventLabel", eventLabel,
                            "ecommerce", cartMap)
            );
    }

    public void eventClickAddToCartImpressionAtcSuccess() {
        if (analyticTracker != null)
            analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                    ConstantTransactionAnalytics.EventCategory.ADD_TO_CART,
                    ConstantTransactionAnalytics.EventAction.IMPRESSION_ATC_SUCCESS,
                    ""
            );
    }

    public void eventClickAddToCartClickBayarOnAtcSuccess() {
        if (analyticTracker != null)
            analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                    ConstantTransactionAnalytics.EventCategory.ADD_TO_CART,
                    ConstantTransactionAnalytics.EventAction.CLICK_BAYAR_ON_ATC_SUCCESS,
                    ""
            );
    }

    public void eventClickAddToCartClickLanjutkanBelanjaOnAtcSuccess() {
        if (analyticTracker != null)
            analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                    ConstantTransactionAnalytics.EventCategory.ADD_TO_CART,
                    ConstantTransactionAnalytics.EventAction.CLICK_LANJUTKAN_BELANJA_ON_ATC_SUCCESS,
                    ""
            );
    }
}
