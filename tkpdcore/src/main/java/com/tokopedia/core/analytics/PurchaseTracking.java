package com.tokopedia.core.analytics;

import com.tokopedia.core.analytics.nishikino.model.Purchase;

/**
 * Created by okasurya on 12/8/17.
 */

public class PurchaseTracking extends TrackingUtils {
    public static final String TRANSACTION = "transaction";
    public static final String PURCHASE = "purchase";
    public static final String EVENT = "event";
    public static final String EVENT_CATEGORY = "eventCategory";
    public static final String PAYMENT_ID = "payment_id";
    public static final String PAYMENT_STATUS = "payment_status";
    public static final String PAYMENT_TYPE = "payment_type";
    public static final String SHOP_ID = "shop_id";
    public static final String LOGISTIC_TYPE = "logistic_type";
    public static final String ECOMMERCE = "ecommerce";

    public static final String USER_ID = "userId";

    public static void marketplace(Purchase purchase) {
        getGTMEngine().eventPurchaseMarketplace(purchase);
        getGTMEngine().sendScreen(AppScreen.SCREEN_FINISH_TX);
        getGTMEngine().clearEnhanceEcommerce();
    }

    public static void digital(Purchase purchase) {
        getGTMEngine().clearEnhanceEcommerce();
        getGTMEngine().eventPurchaseDigital(purchase);
        getGTMEngine().sendScreen(AppScreen.SCREEN_FINISH_TX);
    }
}
