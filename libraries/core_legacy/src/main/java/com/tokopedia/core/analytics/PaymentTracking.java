package com.tokopedia.core.analytics;

import android.content.Context;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.nishikino.model.Checkout;
import com.tokopedia.core.analytics.nishikino.model.Purchase;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.track.TrackApp;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * @author by Herdi_WORK on 25.10.16.
 */

public class PaymentTracking extends TrackingUtils {

    public static void eventTransactionGTM(Context context, Purchase purchase) {
        getGTMEngine(context).eventTransaction(purchase);
        getGTMEngine(context).sendScreen(AppScreen.SCREEN_FINISH_TX_OLD);
        getGTMEngine(context).clearTransactionDataLayer(purchase);
    }

    /* new from TopPayActivity revamped*/
    public static void eventTransactionAF(Context context,
                                          String paymentId,
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

        afValue.put(Jordan.AF_PURCHASE_SITE,Jordan.VALUE_ANDROID);
        afValue.put(AFInAppEventParameterName.CURRENCY, Jordan.VALUE_IDR);
        afValue.put(Jordan.AF_VALUE_PRODUCT_TYPE, productList);
        if(productList != null && productList.length>1) {
            afValue.put(AFInAppEventParameterName.CONTENT_TYPE, Jordan.AF_VALUE_PRODUCTGROUPTYPE);
        }else {
            afValue.put(AFInAppEventParameterName.CONTENT_TYPE, Jordan.AF_VALUE_PRODUCTTYPE);
        }

        TrackApp.getInstance().getAppsFlyer().sendTrackEvent(AFInAppEventType.PURCHASE, afValue);
        TrackApp.getInstance().getAppsFlyer().sendTrackEvent(Jordan.AF_KEY_CRITEO, afValue);
    }

    public static void atcAF(Context context,Map<String, Object> values) {
        TrackApp.getInstance().getAppsFlyer().sendTrackEvent(AFInAppEventType.ADD_TO_CART, values);
    }

    public static void checkoutEventAppsflyer(Context context,ProductCartPass param) {
        Map<String, Object> values = new HashMap<>();
        values.put(AFInAppEventParameterName.CONTENT_ID, param.getProductId());
        values.put(AFInAppEventParameterName.CONTENT_TYPE, Jordan.AF_VALUE_PRODUCTTYPE);
        values.put(AFInAppEventParameterName.DESCRIPTION, param.getProductName());
        values.put(AFInAppEventParameterName.CURRENCY, Jordan.VALUE_IDR);
        values.put(AFInAppEventParameterName.QUANTITY, param.getOrderQuantity());
        values.put(AFInAppEventParameterName.PRICE, param.getPrice());
        values.put(Jordan.AF_SHOP_ID,param.getShopId());
        TrackApp.getInstance().getAppsFlyer().sendTrackEvent(AFInAppEventType.INITIATED_CHECKOUT, values);
    }

    public static void eventCartCheckoutStep1(Context context,Checkout checkout) {
        getGTMEngine(context)
                .eventCheckout(checkout)
                .sendScreen(AppScreen.SCREEN_CART_PAGE)
                .clearCheckoutDataLayer();
    }

    public static void eventCartCheckoutStep2(Context context,Checkout checkout, String paymentId) {
        getGTMEngine(context)
                .eventCheckout(checkout, paymentId)
                .sendScreen(AppScreen.SCREEN_CART_SUMMARY_CHECKOUT)
                .clearCheckoutDataLayer();
    }

}
