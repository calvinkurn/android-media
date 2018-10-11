package com.tokopedia.core.analytics;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.nishikino.model.Product;
import com.tokopedia.core.analytics.nishikino.model.Purchase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static final String PAYMENT_ID = "payment_id";
    public static final String PAYMENT_STATUS = "payment_status";
    public static final String PAYMENT_TYPE = "payment_type";
    public static final String SHOP_ID = "shop_id";
    public static final String LOGISTIC_TYPE = "logistic_type";
    public static final String ECOMMERCE = "ecommerce";
    public static final String EVENT_LABEL = "";

    public static final String USER_ID = "userId";

    public static void marketplace(Purchase purchase) {
        getGTMEngine().eventPurchaseMarketplace(purchase);
        getGTMEngine().sendScreen(AppScreen.SCREEN_FINISH_TX);
        getGTMEngine().clearEnhanceEcommerce();
    }

    public static void digital(Purchase purchase) {
        getGTMEngine().clearEnhanceEcommerce();
        getGTMEngine().eventPurchaseDigital(purchase);
        appsFlyerPurchaseEvent(purchase,"Digital");
        getGTMEngine().sendScreen(AppScreen.SCREEN_FINISH_TX);
    }

    private static int parseStringToInt(String input){

        try{
            return Integer.parseInt(input);
        }catch(NumberFormatException e){
            return 0;
        }
    }

    public static void appsFlyerPurchaseEvent(Purchase trackignData,String productType) {
        Map<String, Object> afValue = new HashMap<>();
        int quantity =  0;
        List<String> productList = new ArrayList<>();
        List<String> productId = new ArrayList<>();
        for(Object product:trackignData.getListProduct()) {
            Map<String, Object> product1 = (Map<String, Object>) product;
            quantity += parseStringToInt(String.valueOf(product1.get(KEY_QTY)));
            productList.add(String.valueOf(product1.get(KEY_NAME)));
            productId.add(String.valueOf(product1.get(KEY_ID)));

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
        if(productList != null && productList.size()>1) {
            afValue.put(AFInAppEventParameterName.CONTENT_TYPE, Jordan.AF_VALUE_PRODUCTGROUPTYPE);
        }else {
            afValue.put(AFInAppEventParameterName.CONTENT_TYPE, Jordan.AF_VALUE_PRODUCTTYPE);
        }

        getAFEngine().sendTrackEvent(AFInAppEventType.PURCHASE, afValue);
        getAFEngine().sendTrackEvent(Jordan.AF_KEY_CRITEO, afValue);
    }
}
