package com.tokopedia.transactionanalytics;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import java.util.Map;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 18/05/18.
 */
public class CheckoutAnalyticProductDetailPage extends CheckoutAnalytics {

    @Inject
    public CheckoutAnalyticProductDetailPage(AnalyticTracker analyticTracker) {
        super(analyticTracker);
    }

    public void enhancedECommerceAddToCart(Map<String, Object> cartMap) {
        if (analyticTracker != null)
            analyticTracker.sendEnhancedEcommerce(
                    DataLayer.mapOf("event", "addToCart",
                            "eventCategory", "",
                            "eventAction", ConstantTransactionAnalytics.EventCategory.ADD_TO_CART,
                            "eventLabel", ConstantTransactionAnalytics.EventLabel.CLICK_BELI,
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
