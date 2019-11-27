package com.tokopedia.core.analytics;

import android.content.Context;
import android.os.Bundle;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.nishikino.model.Purchase;
import com.tokopedia.track.TrackApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.Pair;

import static com.tokopedia.core.analytics.nishikino.model.Product.KEY_CAT;
import static com.tokopedia.core.analytics.nishikino.model.Product.KEY_ID;
import static com.tokopedia.core.analytics.nishikino.model.Product.KEY_NAME;
import static com.tokopedia.core.analytics.nishikino.model.Product.KEY_QTY;

/**
 * Created by okasurya on 12/8/17.
 */

public class PurchaseTracking extends TrackingUtils {
    public static final String TRANSACTION = "transaction";
    public static final String PURCHASE = "purchase";
    public static final String EVENT = "event";
    public static final String EVENT_CATEGORY = "order complete";
    public static final String EVENT_ACTION_DEFAULT = "default";
    public static final String EVENT_ACTION_COD = "view thank you cod";
    public static final String PAYMENT_ID = "payment_id";
    public static final String PAYMENT_STATUS = "payment_status";
    public static final String PAYMENT_TYPE = "payment_type";
    public static final String SHOP_ID = "shop_id";
    public static final String LOGISTIC_TYPE = "logistic_type";
    public static final String ECOMMERCE = "ecommerce";
    public static final String EVENT_LABEL = "";
    public static final String ITEMS = "items";

    public static final String USER_ID = "userId";

    public static void marketplace(Context context, Pair<Purchase, Bundle> purchaseBundlePair) {
        Purchase purchase = purchaseBundlePair.getFirst();
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                AppEventTracking.EVENT, PurchaseTracking.TRANSACTION,
                AppEventTracking.EVENT_CATEGORY, purchase.getEventCategory(),
                AppEventTracking.EVENT_ACTION, purchase.getEventAction(),
                AppEventTracking.EVENT_LABEL, purchase.getEventLabel(),
                Purchase.SHOP_ID, purchase.getShopId(),
                Purchase.PAYMENT_ID, purchase.getPaymentId(),
                Purchase.PAYMENT_TYPE, purchase.getPaymentType(),
                Purchase.LOGISTIC_TYPE, purchase.getLogisticType(),
                Purchase.USER_ID, purchase.getUserId(),
                Purchase.CURRENT_SITE, purchase.getCurrentSite(),
                AppEventTracking.ECOMMERCE, DataLayer.mapOf(
                        Purchase.PURCHASE, purchase.getPurchase(),
                        Purchase.CURRENCY_CODE, purchase.getCurrency()
                )
        ));
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(AppScreen.SCREEN_FINISH_TX);
        TrackApp.getInstance().getGTM().clearEnhanceEcommerce();

        marketplacev5(context, purchaseBundlePair);
    }

    private static void marketplacev5(Context context, Pair<Purchase, Bundle> purchaseBundlePair) {
        Purchase purchase = purchaseBundlePair.getFirst();
        Bundle ecommerceBundle = new Bundle();
        ecommerceBundle.putString(AppEventTracking.EVENT_CATEGORY, purchase.getEventCategory());
        ecommerceBundle.putString(AppEventTracking.EVENT_ACTION, purchase.getEventAction());
        ecommerceBundle.putString(AppEventTracking.EVENT_LABEL, purchase.getEventLabel());
        ecommerceBundle.putString(Purchase.PAYMENT_ID, purchase.getPaymentId());
        ecommerceBundle.putString(Purchase.PAYMENT_TYPE, purchase.getPaymentType());
        ecommerceBundle.putString(Purchase.SHOP_ID, purchase.getShopId());
        ecommerceBundle.putString(Purchase.LOGISTIC_TYPE, purchase.getLogisticType());
        ecommerceBundle.putString(Purchase.CURRENT_SITE, purchase.getCurrentSite());
        ecommerceBundle.putString(Purchase.USER_ID, purchase.getUserId());
        Object transactionID = purchase.getTransactionID();
        ecommerceBundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, transactionID instanceof String ? ((String) transactionID) : "");
        Object affiliation = purchase.getAffiliation();
        ecommerceBundle.putString(FirebaseAnalytics.Param.AFFILIATION, affiliation instanceof String ? ((String) affiliation) : "");
        Object revenue = purchase.getRevenue();
        ecommerceBundle.putDouble(FirebaseAnalytics.Param.VALUE, revenue instanceof String ? Double.parseDouble(((String) revenue)) : 0);
        Object tax = purchase.getTax();
        ecommerceBundle.putFloat(FirebaseAnalytics.Param.TAX, tax instanceof String ? Float.parseFloat(((String) tax)) : 0);
        Object shipping = purchase.getShipping();
        ecommerceBundle.putFloat(FirebaseAnalytics.Param.SHIPPING, shipping instanceof String ? Float.parseFloat(((String) shipping)) : 0);
        ecommerceBundle.putString(FirebaseAnalytics.Param.CURRENCY, purchase.getCurrency());
        Object couponCode = purchase.getCouponCode();
        ecommerceBundle.putString(FirebaseAnalytics.Param.COUPON, couponCode instanceof String ? ((String) couponCode) : "");
        ecommerceBundle.putParcelableArrayList(ITEMS, purchaseBundlePair.getSecond().getParcelableArrayList("products"));
        TrackApp.getInstance().getGTM().pushEECommerce(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, ecommerceBundle);
        TrackApp.getInstance().getGTM().sendScreenV5(AppScreen.SCREEN_FINISH_TX);
    }

    public static void digital(Context context, Purchase purchase) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        AppEventTracking.EVENT, PurchaseTracking.TRANSACTION,
                        AppEventTracking.EVENT_CATEGORY, "digital - thanks",
                        AppEventTracking.EVENT_ACTION, "view purchase attempt",
                        AppEventTracking.EVENT_LABEL, purchase.getEventLabel(),
                        Purchase.SHOP_ID, purchase.getShopId(),
                        Purchase.PAYMENT_ID, purchase.getPaymentId(),
                        Purchase.PAYMENT_TYPE, purchase.getPaymentType(),
                        Purchase.USER_ID, purchase.getUserId(),
                        Purchase.PAYMENT_STATUS, purchase.getPaymentStatus(),
                        Purchase.CURRENT_SITE, purchase.getCurrentSite(),
                        AppEventTracking.ECOMMERCE, DataLayer.mapOf(
                                Purchase.PURCHASE, purchase.getPurchase()
                        )
                )
        );
        appsFlyerPurchaseEvent(context, purchase,"Digital");
        TrackApp.getInstance().getGTM().sendScreenAuthenticated("/digital/thanks");
    }

    private static int parseStringToInt(String input){

        try{
            return Integer.parseInt(input);
        }catch(NumberFormatException e){
            return 0;
        }
    }

    public static void appsFlyerPurchaseEvent(Context context, Purchase trackignData,String productType) {
        Map<String, Object> afValue = new HashMap<>();
        int quantity =  0;
        List<String> productList = new ArrayList<>();
        List<String> productId = new ArrayList<>();
        List<String> productCategory = new ArrayList<>();
        for(Object product:trackignData.getListProduct()) {
            Map<String, Object> product1 = (Map<String, Object>) product;
            quantity += parseStringToInt(String.valueOf(product1.get(KEY_QTY)));
            productList.add(String.valueOf(product1.get(KEY_NAME)));
            productId.add(String.valueOf(product1.get(KEY_ID)));
            productCategory.add(String.valueOf(product1.get(KEY_CAT)));
        }


        afValue.put(AFInAppEventParameterName.REVENUE, trackignData.getRevenue());
        afValue.put(AFInAppEventParameterName.CONTENT_ID, productId);
        afValue.put(AFInAppEventParameterName.QUANTITY, quantity);
        afValue.put(AFInAppEventParameterName.RECEIPT_ID, trackignData.getPaymentId());
        afValue.put(AFInAppEventType.ORDER_ID, trackignData.getTransactionID());

        afValue.put(Jordan.AF_SHIPPING_PRICE,String.valueOf(trackignData.getShipping()));
        afValue.put(Jordan.AF_PURCHASE_SITE,productType);
        afValue.put(AFInAppEventParameterName.CURRENCY, Jordan.VALUE_IDR);
        afValue.put(Jordan.AF_VALUE_PRODUCTTYPE, productList);
        afValue.put(Jordan.AF_KEY_CATEGORY_NAME,productCategory);
        if(productList != null && productList.size()>1) {
            afValue.put(AFInAppEventParameterName.CONTENT_TYPE, Jordan.AF_VALUE_PRODUCTGROUPTYPE);
        }else {
            afValue.put(AFInAppEventParameterName.CONTENT_TYPE, Jordan.AF_VALUE_PRODUCTTYPE);
        }

        TrackApp.getInstance().getAppsFlyer().sendTrackEvent(AFInAppEventType.PURCHASE, afValue);
        TrackApp.getInstance().getAppsFlyer().sendTrackEvent(Jordan.AF_KEY_CRITEO, afValue);
    }
}
