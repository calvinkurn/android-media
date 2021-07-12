package com.tokopedia.checkout.analytics

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics
import javax.inject.Inject

class CheckoutAnalyticsPurchaseProtection @Inject constructor() : TransactionAnalytics() {
    /**
     * User clicks on pelajari and successfullyt land to corresponding page
     * @param url destination *Pelajari* url
     */
    fun eventClickOnPelajari(url: String?) {
        sendEventCategoryActionLabel(ConstantTransactionAnalytics.EventName.PURCHASE_PROTECTION,
                ConstantTransactionAnalytics.EventCategory.PURCHASE_PROTECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_PELAJARI,
                url)
    }

    /**
     * User clicks on bayar successfully. Please capture the tickmark element on purchase
     * protection, whether being check or not
     * @param label tickmark on purchase protection checkbox
     */
    fun eventClickOnBuy(label: String?) {
        sendEventCategoryActionLabel(ConstantTransactionAnalytics.EventName.PURCHASE_PROTECTION,
                ConstantTransactionAnalytics.EventCategory.PURCHASE_PROTECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_PURCHASE_PROTECTION_PAY,
                label)
    }

    /**
     * Impression of product with pelajari appears AS not all product has the pelajari impression
     */
    fun eventImpressionOfProduct() {
        sendEventCategoryActionLabel(ConstantTransactionAnalytics.EventName.PURCHASE_PROTECTION,
                ConstantTransactionAnalytics.EventCategory.PURCHASE_PROTECTION,
                ConstantTransactionAnalytics.EventAction.IMPRESSION_PELAJARI,
                ConstantTransactionAnalytics.EventLabel.APPEAR)
    }
}