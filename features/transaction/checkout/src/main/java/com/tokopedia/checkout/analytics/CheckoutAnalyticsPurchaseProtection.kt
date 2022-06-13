package com.tokopedia.checkout.analytics

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics
import javax.inject.Inject

class CheckoutAnalyticsPurchaseProtection @Inject constructor() : TransactionAnalytics() {
    /**
     * User clicks on pelajari and successfullyt land to corresponding page
     * @param url destination *Pelajari* url
     */
    fun eventClickOnPelajari(userId: String, insuranceBrand: String?, protectionPrice: Int?, catLvl3Id: String?) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.PURCHASE_PROTECTION_CLICK,
            ConstantTransactionAnalytics.EventCategory.PURCHASE_PROTECTION,
            ConstantTransactionAnalytics.EventAction.PP_CLICK_TOOLTIP,
            "$insuranceBrand - $protectionPrice - $catLvl3Id"
        )
        gtmData[ConstantTransactionAnalytics.ExtraKey.USER_ID] = userId
        gtmData[ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_FINTECH
        gtmData[ConstantTransactionAnalytics.ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE_FINTECH
        sendGeneralEvent(gtmData)
    }

    /**
     * User clicks on bayar successfully. Please capture the tickmark element on purchase
     * protection, whether being check or not
     * @param label tickmark on purchase protection checkbox
     */
    fun eventClickOnBuy(userId: String, labelList: List<String>?) {
        labelList?.forEach { label ->
            sendBayarClickEvent(userId, label)
        }
    }

    private fun sendBayarClickEvent(userId: String, label: String) {
        val gtmData = getGtmData(ConstantTransactionAnalytics.EventName.PURCHASE_PROTECTION_CLICK,
            ConstantTransactionAnalytics.EventCategory.PURCHASE_PROTECTION,
            ConstantTransactionAnalytics.EventAction.CLICK_PURCHASE_PROTECTION_PAY,
            label)
        gtmData[ConstantTransactionAnalytics.ExtraKey.USER_ID] = userId
        gtmData[ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_FINTECH
        gtmData[ConstantTransactionAnalytics.ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE_FINTECH
        sendGeneralEvent(gtmData)
    }

    /**
     * Impression of product with pelajari appears AS not all product has the pelajari impression
     */
    fun eventImpressionOfProduct(userId: String, eventLabelList: List<String>) {
       eventLabelList.forEach {
           sendPurchaseProtectionImpression(userId, it)
       }
    }

    private fun sendPurchaseProtectionImpression(userId: String, label: String) {
        val gtmData = getGtmData(ConstantTransactionAnalytics.EventName.PURCHASE_PROTECTION_IMPRESSION,
            ConstantTransactionAnalytics.EventCategory.PURCHASE_PROTECTION,
            ConstantTransactionAnalytics.EventAction.IMPRESSION_PELAJARI,
            label)
        gtmData[ConstantTransactionAnalytics.ExtraKey.USER_ID] = userId
        gtmData[ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_FINTECH
        gtmData[ConstantTransactionAnalytics.ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE_FINTECH
        sendGeneralEvent(gtmData)
    }
}