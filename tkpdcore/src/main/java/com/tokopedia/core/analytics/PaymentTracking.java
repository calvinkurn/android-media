package com.tokopedia.core.analytics;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.nishikino.model.Checkout;
import com.tokopedia.core.analytics.nishikino.model.Purchase;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.core.util.BranchSdkUtils;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * @author by Herdi_WORK on 25.10.16.
 */

public class PaymentTracking extends TrackingUtils {

    public static void eventTransactionGTM(Purchase purchase) {
        BranchSdkUtils.sendCommerceEvent(purchase, BranchSdkUtils.PRODUCTTYPE_MARKETPLACE);
        getGTMEngine().eventTransaction(purchase);
        getGTMEngine().sendScreen(AppScreen.SCREEN_FINISH_TX_OLD);
        getGTMEngine().clearTransactionDataLayer(purchase);
    }

    /* new from TopPayActivity revamped*/
    public static void eventTransactionAF(String paymentId,
                                          String grandTotalBeforeFee,
                                          JSONArray afJSON,
                                          int qty,
                                          Map[] productList
    ) {
        Map<String, Object> afValue = new HashMap<>();
        afValue.put(AFInAppEventParameterName.REVENUE, grandTotalBeforeFee);
        afValue.put(AFInAppEventParameterName.CONTENT_ID, afJSON.toString());
        afValue.put(AFInAppEventParameterName.QUANTITY, qty);
        afValue.put(AFInAppEventParameterName.RECEIPT_ID, paymentId);
        afValue.put(AFInAppEventType.ORDER_ID, paymentId);
        afValue.put(AFInAppEventParameterName.CURRENCY, "IDR");
        afValue.put("product", productList);

        getAFEngine().sendTrackEvent(AFInAppEventType.PURCHASE, afValue);
        getAFEngine().sendTrackEvent(Jordan.AF_KEY_CRITEO, afValue);
    }

    public static void atcAF(Map<String, Object> values) {
        getAFEngine().sendTrackEvent(AFInAppEventType.ADD_TO_CART, values);
    }

    public static void checkoutEventAppsflyer(ProductCartPass param) {
        Map<String, Object> values = new HashMap<>();
        values.put(AFInAppEventParameterName.CONTENT_ID, param.getProductId());
        values.put(AFInAppEventParameterName.CONTENT_TYPE, Jordan.AF_VALUE_PRODUCTTYPE);
        values.put(AFInAppEventParameterName.DESCRIPTION, param.getProductName());
        values.put(AFInAppEventParameterName.CURRENCY, "IDR");
        values.put(AFInAppEventParameterName.PRICE, param.getPrice());
        getAFEngine().sendTrackEvent(AFInAppEventType.INITIATED_CHECKOUT, values);
    }

    public static void eventCartCheckoutStep1(Checkout checkout) {
        getGTMEngine()
                .eventCheckout(checkout)
                .sendScreen(AppScreen.SCREEN_CART_PAGE)
                .clearCheckoutDataLayer();
    }

    public static void eventCartCheckoutStep2(Checkout checkout) {
        getGTMEngine()
                .eventCheckout(checkout)
                .sendScreen(AppScreen.SCREEN_CART_SUMMARY_CHECKOUT)
                .clearCheckoutDataLayer();
    }

}
