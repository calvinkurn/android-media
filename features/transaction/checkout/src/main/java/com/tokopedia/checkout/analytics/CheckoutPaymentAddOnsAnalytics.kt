package com.tokopedia.checkout.analytics

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class CheckoutPaymentAddOnsAnalytics @Inject constructor(
    private val userSession: UserSessionInterface
) : TransactionAnalytics() {

    companion object {
        // Event Action
        const val EVENT_IMPRESSION_CROSS_SELL_ICON = "impression cross sell icon"
        const val EVENT_CHECK_CROSS_SELL_ICON = "check cross sell icon"
        const val EVENT_UNCHECK_CROSS_SELL_ICON = "uncheck cross sell icon"
        const val EVENT_CLICK_PAYMENT_METHOD_WITH_CROSS_SELL =
            "click pilih metode pembayaran - cross sell"

        // Tracker ID
        const val EVENT_IMPRESSION_CROSS_SELL_ICON_TRACKER_ID = "17226"
        const val EVENT_CHECK_CROSS_SELL_ICON_TRACKER_ID = "17227"
        const val EVENT_UNCHECK_CROSS_SELL_ICON_TRACKER_ID = "17228"
        const val EVENT_CLICK_PAYMENT_METHOD_WITH_CROSS_SELL_TRACKER_ID = "17229"
    }

    fun eventImpressCrossSellIcon(
        categoryName: String,
        crossSellProductId: String,
        productCatIds: List<Long>
    ) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.VIEW_ITEM,
            ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
            EVENT_IMPRESSION_CROSS_SELL_ICON,
            generateEventLabel(categoryName, crossSellProductId, productCatIds)
        )

        gtmData[ConstantTransactionAnalytics.ExtraKey.TRACKER_ID] =
            EVENT_IMPRESSION_CROSS_SELL_ICON_TRACKER_ID
        gtmData[ConstantTransactionAnalytics.Key.CURRENT_SITE] =
            ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT] =
            ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ConstantTransactionAnalytics.ExtraKey.USER_ID] = userSession.userId
        gtmData[ConstantTransactionAnalytics.ExtraKey.PROMOTIONS] = mutableMapOf<String, String>()

        sendEnhancedEcommerce(gtmData)
    }

    fun eventCheckCrossSellIcon(
        categoryName: String,
        crossSellProductId: String,
        productCatIds: List<Long>
    ) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.SELECT_CONTENT,
            ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
            EVENT_CHECK_CROSS_SELL_ICON,
            generateEventLabel(categoryName, crossSellProductId, productCatIds)
        )

        gtmData[ConstantTransactionAnalytics.ExtraKey.TRACKER_ID] =
            EVENT_CHECK_CROSS_SELL_ICON_TRACKER_ID
        gtmData[ConstantTransactionAnalytics.Key.CURRENT_SITE] =
            ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT] =
            ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ConstantTransactionAnalytics.ExtraKey.USER_ID] = userSession.userId
        gtmData[ConstantTransactionAnalytics.ExtraKey.PROMOTIONS] = mutableMapOf<String, String>()

        sendEnhancedEcommerce(gtmData)
    }

    fun eventUncheckCrossSellIcon(
        categoryName: String,
        crossSellProductId: String,
        productCatIds: List<Long>
    ) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.SELECT_CONTENT,
            ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
            EVENT_UNCHECK_CROSS_SELL_ICON,
            generateEventLabel(categoryName, crossSellProductId, productCatIds)
        )

        gtmData[ConstantTransactionAnalytics.ExtraKey.TRACKER_ID] =
            EVENT_UNCHECK_CROSS_SELL_ICON_TRACKER_ID
        gtmData[ConstantTransactionAnalytics.Key.CURRENT_SITE] =
            ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT] =
            ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ConstantTransactionAnalytics.ExtraKey.USER_ID] = userSession.userId
        gtmData[ConstantTransactionAnalytics.ExtraKey.PROMOTIONS] = mutableMapOf<String, String>()

        sendEnhancedEcommerce(gtmData)
    }

    fun eventClickPaymentMethodWithCrossSell(
        crossSellData: List<Pair<String, String>>,
        productCatIds: List<Long>
    ) {
        for (crossSell in crossSellData) {
            val (categoryName, crossSellProductId) = crossSell
            val gtmData = getGtmData(
                ConstantTransactionAnalytics.EventName.CLICK_DIGITAL,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                EVENT_CLICK_PAYMENT_METHOD_WITH_CROSS_SELL,
                generateEventLabel(categoryName, crossSellProductId, productCatIds)
            )

            gtmData[ConstantTransactionAnalytics.ExtraKey.TRACKER_ID] =
                EVENT_CLICK_PAYMENT_METHOD_WITH_CROSS_SELL_TRACKER_ID
            gtmData[ConstantTransactionAnalytics.Key.CURRENT_SITE] =
                ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
            gtmData[ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT] =
                ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
            gtmData[ConstantTransactionAnalytics.ExtraKey.USER_ID] = userSession.userId
            gtmData[ConstantTransactionAnalytics.ExtraKey.PROMOTIONS] =
                mutableMapOf<String, String>()

            sendEnhancedEcommerce(gtmData)
        }
    }

    private fun generateEventLabel(
        categoryName: String,
        crossSellProductId: String,
        productCatIds: List<Long>
    ): String {
        return "$categoryName - $crossSellProductId - ${productCatIds.joinToString(",")}"
    }
}
