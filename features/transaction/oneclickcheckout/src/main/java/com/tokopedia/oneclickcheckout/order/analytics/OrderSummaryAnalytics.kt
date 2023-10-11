package com.tokopedia.oneclickcheckout.order.analytics

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.CustomDimension
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventAction
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventCategory
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventName
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.ExtraKey
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.Key
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.TrackerId
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics
import javax.inject.Inject

class OrderSummaryAnalytics @Inject constructor() : TransactionAnalytics() {

    fun eventEditQuantityIncrease(productId: String, shopId: String, productQuantity: String) {
        sendGeneralEvent(
            EventName.CLICK_CHECKOUT_EXPRESS,
            EventCategory.ORDER_SUMMARY,
            EventAction.EDIT_QUANTITY_INCREASE,
            "$productId - $shopId - $productQuantity"
        )
    }

    fun eventEditQuantityDecrease(productId: String, shopId: String, productQuantity: String) {
        sendGeneralEvent(
            EventName.CLICK_CHECKOUT_EXPRESS,
            EventCategory.ORDER_SUMMARY,
            EventAction.EDIT_QUANTITY_DECREASE,
            "$productId - $shopId - $productQuantity"
        )
    }

    fun eventClickSellerNotes(productId: String, shopId: String) {
        sendGeneralEvent(
            EventName.CLICK_CHECKOUT_EXPRESS,
            EventCategory.ORDER_SUMMARY,
            EventAction.EDIT_SELLER_NOTES,
            "$productId - $shopId"
        )
    }

    fun eventChangeCourierOSP(shippingAgentId: String) {
        sendGeneralEvent(
            EventName.CLICK_CHECKOUT_EXPRESS,
            EventCategory.ORDER_SUMMARY,
            EventAction.USER_CHANGE_COURIER_OSP,
            shippingAgentId
        )
    }

    fun eventClickOnInsurance(insuranceCheck: String, insuranceValue: String) {
        val gtmData = getGtmData(
            EventName.CLICK_CHECKOUT_EXPRESS,
            EventCategory.ORDER_SUMMARY,
            EventAction.CLICK_ON_INSURANCE,
            "$insuranceCheck - $insuranceValue"
        )
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventClickOnInsuranceInfoTooltip(userId: String) {
        val gtmData = getGtmData(
            EventName.CLICK_CX,
            EventCategory.INSURANCE_INFO_TOOLTIP,
            EventAction.CLICK_INSURANCE_INFO_TOOLTIP,
            ""
        )
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.TRACKER_ID] = TrackerId.CLICK_INSURANCE_INFO_TOOLTIP
        gtmData[ExtraKey.USER_ID] = userId
        sendGeneralEvent(gtmData)
    }

    fun eventClickBayarNotSuccess(isButtonPilihPembayaran: Boolean, eventId: String) {
        sendGeneralEvent(
            EventName.CLICK_CHECKOUT_EXPRESS,
            EventCategory.ORDER_SUMMARY,
            if (isButtonPilihPembayaran) EventAction.CLICK_PILIH_PEMBAYARAN_NOT_SUCCESS else EventAction.CLICK_BAYAR_NOT_SUCCESS,
            "$NOT_SUCCESS - $eventId"
        )
    }

    fun eventChooseBboAsDuration() {
        sendGeneralEvent(
            EventName.CLICK_CHECKOUT_EXPRESS,
            EventCategory.ORDER_SUMMARY,
            EventAction.CHOOSE_BBO_AS_DURATION
        )
    }

    fun eventViewMessageInCourier2JamSampai(message: String) {
        val gtmData = getGtmData(
            EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
            EventCategory.ORDER_SUMMARY,
            EventAction.VIEW_MESSAGE_IN_COURIER_2_JAM_SAMPAI,
            message
        )
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventChooseCourierSelectionOSP(shippingAgentId: String) {
        sendGeneralEvent(
            EventName.CLICK_CHECKOUT_EXPRESS,
            EventCategory.ORDER_SUMMARY,
            EventAction.CHOOSE_COURIER_FROM_COURIER_SELECTION_OSP,
            shippingAgentId
        )
    }

    fun eventClickRingkasanBelanjaOSP(totalPrice: String) {
        val gtmData = getGtmData(
            EventName.CLICK_CHECKOUT_EXPRESS,
            EventCategory.ORDER_SUMMARY,
            EventAction.CLICK_RINGKASAN_BELANJA_OSP,
            totalPrice
        )
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventClickBackFromOSP() {
        sendGeneralEvent(
            EventName.CLICK_CHECKOUT_EXPRESS,
            EventCategory.ORDER_SUMMARY,
            EventAction.CLICK_BACK_FROM_OSP
        )
    }

    fun eventViewOrderSummaryPage(userId: String, paymentType: String, ee: Map<String, Any>) {
        val dataLayer = getGtmData(
            EventName.CHECKOUT,
            EventCategory.ORDER_SUMMARY,
            EventAction.VIEW_ORDER_SUMMARY_PAGE,
            "success"
        )
        dataLayer[Key.E_COMMERCE] = ee
        dataLayer[ExtraKey.USER_ID] = userId
        dataLayer[ExtraKey.PAYMENT_METHOD] = paymentType
        dataLayer[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        dataLayer[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        sendEnhancedEcommerce(dataLayer)
    }

    fun eventClickBayarSuccess(
        isButtonPilihPembayaran: Boolean,
        userId: String,
        paymentId: String,
        paymentType: String,
        tenureType: String,
        ee: Map<String, Any>
    ) {
        val dataLayer = getGtmData(
            EventName.CHECKOUT,
            EventCategory.ORDER_SUMMARY,
            if (isButtonPilihPembayaran) EventAction.CLICK_PILIH_PEMBAYARAN else EventAction.CLICK_BAYAR,
            "success - $paymentType - $tenureType"
        )
        dataLayer[Key.E_COMMERCE] = ee
        dataLayer[Key.PAYMENT_ID] = paymentId
        dataLayer[ExtraKey.USER_ID] = userId
        dataLayer[ExtraKey.PAYMENT_METHOD] = paymentType
        dataLayer[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        dataLayer[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        if (!isButtonPilihPembayaran) {
            dataLayer[ExtraKey.TRACKER_ID] = TrackerId.CLICK_BAYAR_OCC
        }
        sendEnhancedEcommerce(dataLayer)
    }

    fun eventClickPromoOSP(promoCodes: List<String>) {
        if (promoCodes.isEmpty()) {
            sendGeneralEvent(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CLICK_PROMO_SECTION_NOT_APPLIED_OSP
            )
        } else {
            sendGeneralEvent(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CLICK_PROMO_SECTION_APPLIED_OSP,
                promoCodes.joinToString("-")
            )
        }
    }

    fun eventViewBottomSheetPromoError() {
        sendGeneralEvent(
            EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
            EventCategory.EXPRESS_CHECKOUT,
            EventAction.VIEW_BOTTOMSHEET_PROMO_ERROR
        )
    }

    fun eventClickLanjutBayarPromoErrorOSP() {
        sendGeneralEvent(
            EventName.CLICK_CHECKOUT_EXPRESS,
            EventCategory.ORDER_SUMMARY,
            EventAction.CLICK_LANJUT_BAYAR_PROMO_ERROR_OSP
        )
    }

    fun eventClickPilihPromoLainPromoErrorOSP() {
        sendGeneralEvent(
            EventName.CLICK_CHECKOUT_EXPRESS,
            EventCategory.ORDER_SUMMARY,
            EventAction.CLICK_PILIH_PROMO_LAIN_PROMO_ERROR_OSP
        )
    }

    fun eventViewPromoAlreadyApplied() {
        sendGeneralEvent(
            EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
            EventCategory.ORDER_SUMMARY,
            EventAction.VIEW_PROMO_ALREADY_APPLIED
        )
    }

    fun eventViewPromoDecreasedOrReleased(isReleased: Boolean) {
        sendGeneralEvent(
            EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
            EventCategory.ORDER_SUMMARY,
            if (isReleased) EventAction.VIEW_PROMO_RELEASED else EventAction.VIEW_PROMO_DECREASED
        )
    }

    fun eventViewErrorMessage(error: String) {
        sendGeneralEvent(
            EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
            EventCategory.ORDER_SUMMARY,
            EventAction.VIEW_ERROR_ON_OSP,
            error
        )
    }

    fun eventViewPreselectedCourierOption(spId: String, userId: String) {
        val gtmData = getGtmData(
            EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
            EventCategory.ORDER_SUMMARY,
            EventAction.VIEW_PRESELECTED_COURIER_OPTION,
            spId
        )
        gtmData[ExtraKey.USER_ID] = userId
        sendGeneralEvent(gtmData)
    }

    fun eventClickRefreshOnCourierSection(shopId: String) {
        val gtmData = getGtmData(
            EventName.CLICK_CHECKOUT_EXPRESS,
            EventCategory.ORDER_SUMMARY,
            EventAction.CLICK_REFRESH_ON_COURIER_SECTION,
            shopId
        )
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventClickUbahWhenDurationError(userId: String) {
        val gtmData = getGtmData(
            EventName.CLICK_CHECKOUT_EXPRESS,
            EventCategory.ORDER_SUMMARY,
            EventAction.CLICK_UBAH_WHEN_DURATION_ERROR,
            ""
        )
        gtmData[ExtraKey.USER_ID] = userId
        sendGeneralEvent(gtmData)
    }

    fun eventClickArrowToChangeAddressOption(currentAddressId: String, userId: String) {
        val gtmData = getGtmData(
            EventName.CLICK_CHECKOUT_EXPRESS,
            EventCategory.ORDER_SUMMARY,
            EventAction.CLICK_ARROW_TO_CHANGE_ADDRESS_OPTION,
            currentAddressId
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventClickSelectedAddressOption(newAddressId: String, userId: String) {
        val gtmData = getGtmData(
            EventName.CLICK_CHECKOUT_EXPRESS,
            EventCategory.ORDER_SUMMARY,
            EventAction.CLICK_SELECTED_ADDRESS_OPTION,
            newAddressId
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventClickArrowToChangeDurationOption(currentSpId: String, userId: String) {
        val gtmData = getGtmData(
            EventName.CLICK_CHECKOUT_EXPRESS,
            EventCategory.ORDER_SUMMARY,
            EventAction.CLICK_ARROW_TO_CHANGE_DURATION_OPTION,
            currentSpId
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventClickSelectedDurationOptionNew(newSpId: String, userId: String) {
        val gtmData = getGtmData(
            EventName.CLICK_CHECKOUT_EXPRESS,
            EventCategory.ORDER_SUMMARY,
            EventAction.CLICK_SELECTED_DURATION_OPTION_NEW,
            newSpId
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventClickArrowToChangePaymentOption(currentGateway: String, userId: String) {
        val gtmData = getGtmData(
            EventName.CLICK_CHECKOUT_EXPRESS,
            EventCategory.ORDER_SUMMARY,
            EventAction.CLICK_ARROW_TO_CHANGE_PAYMENT_OPTION,
            currentGateway
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventClickSelectedPaymentOption(newGateway: String, userId: String) {
        val gtmData = getGtmData(
            EventName.CLICK_CHECKOUT_EXPRESS,
            EventCategory.ORDER_SUMMARY,
            EventAction.CLICK_SELECTED_PAYMENT_OPTION,
            newGateway
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventViewCoachmark1ForNewBuyerAfterCreateProfile(userId: String) {
        val gtmData = getGtmData(
            EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
            EventCategory.ORDER_SUMMARY,
            EventAction.VIEW_COACHMARK_1_FOR_NEW_BUYER_AFTER_CREATE_PROFILE,
            ""
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventViewCoachmark2ForNewBuyerAfterCreateProfile(userId: String) {
        val gtmData = getGtmData(
            EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
            EventCategory.ORDER_SUMMARY,
            EventAction.VIEW_COACHMARK_2_FOR_NEW_BUYER_AFTER_CREATE_PROFILE,
            ""
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventViewCoachmark3ForNewBuyerAfterCreateProfile(userId: String) {
        val gtmData = getGtmData(
            EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
            EventCategory.ORDER_SUMMARY,
            EventAction.VIEW_COACHMARK_3_FOR_NEW_BUYER_AFTER_CREATE_PROFILE,
            ""
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventClickDoneOnCoachmark3ForNewBuyerAfterCreateProfile(userId: String) {
        val gtmData = getGtmData(
            EventName.CLICK_CHECKOUT_EXPRESS,
            EventCategory.ORDER_SUMMARY,
            EventAction.CLICK_DONE_ON_COACHMARK_3_FOR_NEW_BUYER_AFTER_CREATE_PROFILE,
            ""
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventPPImpressionOnInsuranceSection(
        userId: String,
        categoryLvl3Id: String,
        protectionPrice: Int,
        protectionName: String
    ) {
        val gtmData = getGtmData(
            EventName.PURCHASE_PROTECTION_IMPRESSION,
            EventCategory.PURCHASE_PROTECTION_OCC,
            EventAction.PP_IMPRESSION_ON_INSURANCE_SECTION,
            "$protectionName - $protectionPrice - $categoryLvl3Id"
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_FINTECH
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE_FINTECH
        sendGeneralEvent(gtmData)
    }

    fun eventPPClickTooltip(
        userId: String,
        categoryLvl3Id: String,
        protectionPrice: Int,
        protectionName: String
    ) {
        val gtmData = getGtmData(
            EventName.PURCHASE_PROTECTION_CLICK,
            EventCategory.PURCHASE_PROTECTION_OCC,
            EventAction.PP_CLICK_TOOLTIP,
            "$protectionName - $protectionPrice - $categoryLvl3Id"
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_FINTECH
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE_FINTECH
        sendGeneralEvent(gtmData)
    }

    fun eventPPClickBayar(
        userId: String,
        categoryLvl3Id: String?,
        protectionName: String?,
        ppPrice: Int?,
        cartId: String?,
        isChecked: String
    ) {
        val gtmData = getGtmData(
            EventName.PURCHASE_PROTECTION_CLICK,
            EventCategory.PURCHASE_PROTECTION_OCC,
            EventAction.PP_CLICK_BAYAR,
            "$protectionName - $isChecked - $categoryLvl3Id - " +
                "${ppPrice ?: 0} - ${cartId ?: ""}"
        )
        gtmData[ExtraKey.CART_ID] = cartId ?: ""
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_FINTECH
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE_FINTECH
        sendGeneralEvent(gtmData)
    }

    fun eventViewOverweightTicker(shopId: String) {
        val gtmData = getGtmData(
            EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
            EventCategory.ORDER_SUMMARY,
            EventAction.VIEW_OVERWEIGHT_TICKER,
            shopId
        )
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventViewErrorProductLevelTicker(shopId: String, message: String) {
        val gtmData = getGtmData(
            EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
            EventCategory.ORDER_SUMMARY,
            EventAction.VIEW_ERROR_PRODUCT_LEVEL_TICKER,
            "$shopId - $message"
        )
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventViewErrorOrderLevelTicker(shopId: String, message: String) {
        val gtmData = getGtmData(
            EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
            EventCategory.ORDER_SUMMARY,
            EventAction.VIEW_ERROR_ORDER_LEVEL_TICKER,
            "$shopId - $message"
        )
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventViewErrorToasterMessage(shopId: String, message: String) {
        val gtmData = getGtmData(
            EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
            EventCategory.ORDER_SUMMARY,
            EventAction.VIEW_ERROR_TOASTER_MESSAGE,
            "$shopId - $message"
        )
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventViewTopUpGoPayButton() {
        val gtmData = getGtmData(
            EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
            EventCategory.ORDER_SUMMARY,
            EventAction.VIEW_TOP_UP_GOPAY_BUTTON,
            ""
        )
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventClickTopUpGoPayButton() {
        val gtmData = getGtmData(
            EventName.CLICK_CHECKOUT_EXPRESS,
            EventCategory.ORDER_SUMMARY,
            EventAction.CLICK_TOP_UP_GOPAY_BUTTON,
            ""
        )
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventViewPaymentMethod(paymentType: String) {
        val gtmData = getGtmData(
            EventName.VIEW_PP_IRIS,
            EventCategory.ORDER_SUMMARY,
            EventAction.VIEW_PAYMENT_METHOD,
            paymentType
        )
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventViewTenureOption(tenureType: String) {
        val gtmData = getGtmData(
            EventName.VIEW_PP_IRIS,
            EventCategory.ORDER_SUMMARY,
            EventAction.VIEW_TENURE_OPTION,
            tenureType
        )
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventClickTenureOptionsBottomSheet() {
        val gtmData = getGtmData(
            EventName.CLICK_PP,
            EventCategory.ORDER_SUMMARY,
            EventAction.CLICK_TENURE_OPTIONS_BOTTOMSHEET,
            ""
        )
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventViewAddOnsWidget(productId: String) {
        val gtmData = getGtmData(
            EventName.VIEW_PP_IRIS,
            EventCategory.ORDER_SUMMARY,
            EventAction.VIEW_ADD_ONS_WIDGET,
            productId
        )
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventClickAddOnsDetail(productId: String) {
        val gtmData = getGtmData(
            EventName.CLICK_PP,
            EventCategory.ORDER_SUMMARY,
            EventAction.CLICK_ADD_ONS_DETAIL,
            productId
        )
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventViewGoToPlusBadge() {
        val gtmData = getGtmData(
            EventName.VIEW_PP_IRIS,
            EventCategory.ORDER_SUMMARY,
            EventAction.VIEW_GOTOPLUS_TICKER,
            ""
        )
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.TRACKER_ID] = TrackerId.VIEW_GOTOPLUS_TICKER_OCC
        sendGeneralEvent(gtmData)
    }

    fun eventViewAddOnProductWidget(addOnType: Int, productId: String) {
        val gtmData = getGtmData(
            EventName.VIEW_PP_IRIS,
            EventCategory.ORDER_SUMMARY,
            EventAction.VIEW_ADD_ONS_PRODUCT_WIDGET,
            "$addOnType - $productId"
        )
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.TRACKER_ID] = TrackerId.VIEW_ADDONS_PRODUCT_WIDGET_OCC
        sendGeneralEvent(gtmData)
    }

    fun eventClickAddOnProductWidget(addOnType: Int, productId: String, isChecked: Boolean) {
        val gtmData = getGtmData(
            EventName.CLICK_PP,
            EventCategory.ORDER_SUMMARY,
            EventAction.CLICK_ADD_ONS_PRODUCT_WIDGET,
            "$addOnType - $productId - $isChecked"
        )
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.TRACKER_ID] = TrackerId.CLICK_ADDONS_PRODUCT_WIDGET_OCC
        sendGeneralEvent(gtmData)
    }

    fun eventClickLihatSemuaAddOnProductWidget() {
        val gtmData = getGtmData(
            EventName.CLICK_PP,
            EventCategory.ORDER_SUMMARY,
            EventAction.CLICK_LIHAT_SEMUA_ON_ADDONS_PRODUCT_WIDGET,
            ""
        )
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.TRACKER_ID] = TrackerId.CLICK_LIHAT_SEMUA_ADDONS_PRODUCT_WIDGET_OCC
        sendGeneralEvent(gtmData)
    }

    companion object {
        private const val NOT_SUCCESS = "not success"

        const val ERROR_ID_PRICE_CHANGE = "8"
        const val ERROR_ID_LOGISTIC_DURATION_UNAVAILABLE = "9"
        const val ERROR_ID_LOGISTIC_DISTANCE_EXCEED = "10"
        const val ERROR_ID_LOGISTIC_WEIGHT_EXCEED = "11"
        const val ERROR_ID_LOGISTIC_BBO_MINIMUM = "12"
        const val ERROR_ID_PAYMENT_OVO_BALANCE = "13"
    }
}
