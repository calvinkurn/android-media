package com.tokopedia.purchase_platform.features.checkout.analytics;

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventAction;
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventCategory;
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventLabel;
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventName;
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics;

import javax.inject.Inject;

/**
 * Created by fajarnuha on 11/12/18.
 */
public class CheckoutAnalyticsPurchaseProtection extends TransactionAnalytics {

    @Inject
    public CheckoutAnalyticsPurchaseProtection() {
    }

    /**
     * User clicks on pelajari and successfullyt land to corresponding page
     * @param url destination <em>Pelajari</em> url
     */
    public void eventClickOnPelajari(String url) {
        sendEventCategoryActionLabel(EventName.PURCHASE_PROTECTION,
                EventCategory.PURCHASE_PROTECTION,
                EventAction.CLICK_PELAJARI,
                url);
    }

    /**
     * User clicks on bayar successfully. Please capture the tickmark element on purchase
     * protection, whether being check or not
     * @param label tickmark on purchase protection checkbox
     */
    public void eventClickOnBuy(String label) {
        sendEventCategoryActionLabel(EventName.PURCHASE_PROTECTION,
                EventCategory.PURCHASE_PROTECTION,
                EventAction.CLICK_PURCHASE_PROTECTION_PAY,
                label);
    }

    /**
     * Impression of product with pelajari appears AS not all product has the pelajari impression
     */
    public void eventImpressionOfProduct() {
        sendEventCategoryActionLabel(EventName.PURCHASE_PROTECTION,
                EventCategory.PURCHASE_PROTECTION,
                EventAction.IMPRESSION_PELAJARI,
                EventLabel.APPEAR);
    }
}
