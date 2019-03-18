package com.tokopedia.transactionanalytics;

import com.google.android.gms.tagmanager.DataLayer;

import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.CustomDimension;
import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventAction;
import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventCategory;
import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventName;
import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.Key;


/**
 * @author anggaprasetiyo on 06/06/18.
 */
public class CheckoutAnalyticsAddToCart extends TransactionAnalytics {

    @Inject
    public CheckoutAnalyticsAddToCart() {
    }

    public void enhancedECommerceAddToCartClickBeli(Map<String, Object> cartMap, String eventLabel) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.ADD_TO_CART,
                Key.EVENT_CATEGORY, EventCategory.ADD_TO_CART,
                Key.EVENT_ACTION, EventAction.CLICK_BELI,
                Key.EVENT_LABEL, eventLabel,
                Key.E_COMMERCE, cartMap
        );
        sendEnhancedEcommerce(dataLayer);


    }

    public void enhancedECommerceAddToCart(Map<String, Object> cartMap, String eventLabel, String eventAction) {
        sendEnhancedEcommerce(
                DataLayer.mapOf(
                        Key.EVENT, EventName.ADD_TO_CART,
                        Key.EVENT_CATEGORY, EventCategory.ADD_TO_CART,
                        Key.EVENT_ACTION, eventAction,
                        Key.EVENT_LABEL, eventLabel,
                        Key.E_COMMERCE, cartMap,
                        Key.CURRENT_SITE, CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
                )
        );
    }

    public void flushEnhancedECommerceAddToCart() {
        sendEnhancedEcommerce(
                DataLayer.mapOf(
                        Key.E_COMMERCE, null,
                        Key.CURRENT_SITE, null
                )
        );
    }

    public void eventClickAtcAddToCartImpressionAtcSuccess() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.ADD_TO_CART,
                EventAction.IMPRESSION_ATC_SUCCESS
        );
    }

    public void eventClickAtcAddToCartClickBayarOnAtcSuccess() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.ADD_TO_CART,
                EventAction.CLICK_BAYAR_ON_ATC_SUCCESS
        );
    }

    public void eventClickAtcAddToCartClickLanjutkanBelanjaOnAtcSuccess() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.ADD_TO_CART,
                EventAction.CLICK_LANJUTKAN_BELANJA_ON_ATC_SUCCESS
        );
    }

    public void eventAtcClickLihat(String productId) {
        sendEventCategoryActionLabel(
                EventName.CLICK_PDP,
                EventCategory.PRODUCT_DETAIL_PAGE,
                EventAction.CLICK_CEK_KERANJANG,
                productId
        );
    }
}
