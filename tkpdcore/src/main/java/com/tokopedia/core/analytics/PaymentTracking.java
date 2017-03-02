package com.tokopedia.core.analytics;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.localytics.android.Localytics;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.model.Product;
import com.tokopedia.core.analytics.nishikino.model.Checkout;
import com.tokopedia.core.analytics.nishikino.model.Purchase;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.payment.model.responsecartstep2.Transaction;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author by Herdi_WORK on 25.10.16.
 */

public class PaymentTracking extends TrackingUtils {

    public static void eventTransactionGTM(Purchase purchase) {
        getGTMEngine().eventTransaction(purchase);
        getGTMEngine().sendScreen(AppScreen.SCREEN_FINISH_TX);
        getGTMEngine().clearTransactionDataLayer(purchase);
    }

    public static void eventTransactionLoca(Map<String, String> values, ArrayList<Product> products) {
        String screenName = MainApplication.getAppContext().getString(R.string.cart_pg_3);
        String transcatAttrKey = MainApplication.getAppContext().getString(R.string.event_value_total_transaction);
        getLocaEngine()
                .tagScreen(screenName)
                .tageEventandInApp("event : Viewed " + screenName)
                .tagEvent(MainApplication.getAppContext().getString(R.string.event_finish_transaction), values, Long.parseLong(values.get(transcatAttrKey)))
                .incrementProfileAttribute(transcatAttrKey, Long.parseLong(values.get(transcatAttrKey)), Localytics.ProfileScope.APPLICATION);

        if (products != null && !products.isEmpty()) {
            for (Product product : products) {
                getLocaEngine()
                        .tagPurchasedEvent(product);
            }
        }
    }

    public static void eventTransactionAF(Transaction cartData, ArrayList<String> productIDList, int qty, Map[] productList) {
        JSONArray afJSON = new JSONArray(productIDList);
        eventTransactionAF(cartData, afJSON, qty, productList);
    }

    public static void eventTransactionAF(Transaction cartData, JSONArray afJSON, int qty, Map[] productList) {
        Map<String, Object> afValue = new HashMap<>();
        afValue.put(AFInAppEventParameterName.REVENUE, cartData.getGrandTotalBeforeFee());
        afValue.put(AFInAppEventParameterName.CONTENT_ID, afJSON.toString());
        afValue.put(AFInAppEventParameterName.QUANTITY, qty);
        afValue.put(AFInAppEventParameterName.RECEIPT_ID, cartData.getPaymentId());
        afValue.put(AFInAppEventType.ORDER_ID, cartData.getPaymentId());
        afValue.put(AFInAppEventParameterName.CURRENCY, "IDR");
        afValue.put("product", productList);

        getAFEngine().sendTrackEvent(AFInAppEventType.PURCHASE, afValue);
        getAFEngine().sendTrackEvent(Jordan.AF_KEY_CRITEO, afValue);
    }

    /* new from TopPayActivity revamped*/
    public static void eventTransactionAF(
            String paymentId, String grandTotalBeforeFee, JSONArray afJSON, int qty,
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

    public static void atcLoca(ProductCartPass productCartPass, String productId,
                               String priceItem, Map<String, String> values) {
        eventLoca("event : " + MainApplication.getAppContext().getString(R.string.event_add_to_cart), values);
        getLocaEngine().tagAddedToCart(productCartPass.getProductName(), productId, productCartPass.getProductCategory(), Long.parseLong(priceItem.replace("Rp", "").replace(".", "").trim()), null)
                .setProfileAttribute(
                        MainApplication.getAppContext().getString(R.string.profile_last_date_add_to_cart),
                        new GregorianCalendar().getTime(),
                        Localytics.ProfileScope.APPLICATION);
    }

    public static void atcAF(Map<String, Object> values) {
        getAFEngine().sendTrackEvent(AFInAppEventType.ADD_TO_CART, values);
    }

    public static void confirmPaymentLoca(Map<String, String> values) {
        String transcatAttrKey = MainApplication.getAppContext().getString(R.string.event_value_total_transaction);
        long amtPayment = Long.valueOf(values.get(transcatAttrKey));
        eventLoca(MainApplication.getAppContext().getString(R.string.event_confirm_payment),
                values, amtPayment);
        getLocaEngine().incrementProfileAttribute(transcatAttrKey, amtPayment, Localytics.ProfileScope.APPLICATION);
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

    public static void eventCartCheckout(Checkout checkout) {
        CommonUtils.dumper("GAv4 CHECKOUT EVENT");
        getGTMEngine()
                .eventCheckout(checkout)
                .sendScreen(AppScreen.SCREEN_CART_SUMMARY_CHECKOUT)
                .clearCheckoutDataLayer();
    }
}
