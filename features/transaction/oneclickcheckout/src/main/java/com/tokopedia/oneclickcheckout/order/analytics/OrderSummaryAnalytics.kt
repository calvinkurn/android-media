package com.tokopedia.oneclickcheckout.order.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.*
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics

class OrderSummaryAnalytics : TransactionAnalytics() {

    fun eventEditQuantityIncrease(productId: String, shopId: String, productQuantity: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.EDIT_QUANTITY_INCRESE,
                "$productId - $shopId - $productQuantity"
        )
    }

    fun eventEditQuantityDecrease(productId: String, shopId: String, productQuantity: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.EDIT_QUANTITY_DECREASE,
                "$productId - $shopId - $productQuantity"
        )
    }

    fun eventClickSellerNotes(productId: String, shopId: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.EDIT_SELLER_NOTES,
                "$productId - $shopId"
        )
    }

    fun eventChangeCourierOSP(shippingAgentId: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.USER_CHANGE_COURIER_OSP,
                shippingAgentId
        )
    }

    fun eventClickSimilarProductEmptyStock() {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.FIND_SIMILAR_PRODUCT,
                EventLabel.EMPTY_STOCK
        )
    }

    fun eventClickSimilarProductShopClosed() {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.FIND_SIMILAR_PRODUCT,
                EventLabel.SHOP_CLOSED
        )
    }

    fun eventClickOnInsurance(productId: String, insuranceCheck: String, insuranceValue: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CLICK_ON_INSURANCE,
                "$productId - $insuranceCheck - $insuranceValue"
        )
    }

    fun eventClickBayarNotSuccess(isButtonPilihPembayaran: Boolean, eventId: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                if (isButtonPilihPembayaran) EventAction.CLICK_PILIH_PEMBAYARAN_NOT_SUCCESS else EventAction.CLICK_BAYAR_NOT_SUCCESS,
                "$NOT_SUCCESS - $eventId"
        )
    }

    fun eventChooseBboAsDuration() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CHOOSE_BBO_AS_DURATION
        )
    }

    fun eventChooseCourierSelectionOSP(shippingAgentId: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CHOOSE_COURIER_FROM_COURIER_SELECTION_OSP,
                shippingAgentId
        )
    }

    fun eventClickRingkasanBelanjaOSP(productId: String, totalPrice: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CLICK_RINGKASAN_BELANJA_OSP,
                "$productId - $totalPrice"
        )
    }

    fun eventClickBackFromOSP() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CLICK_BACK_FROM_OSP
        )
    }

    fun eventViewOrderSummaryPage(userId: String, paymentType: String, ee: Map<String, Any>) {
        val dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.CHECKOUT,
                Key.EVENT_CATEGORY, EventCategory.ORDER_SUMMARY,
                Key.EVENT_ACTION, EventAction.VIEW_ORDER_SUMMARY_PAGE,
                Key.EVENT_LABEL, "success",
                Key.E_COMMERCE, ee,
                ExtraKey.USER_ID, userId,
                ExtraKey.PAYMENT_TYPE, paymentType
        )
        sendEnhancedEcommerce(dataLayer)
    }

    fun eventClickBayarSuccess(isButtonPilihPembayaran: Boolean, userId: String, paymentId: String, paymentType: String, ee: Map<String, Any>) {
        val dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.CHECKOUT,
                Key.EVENT_CATEGORY, EventCategory.ORDER_SUMMARY,
                Key.EVENT_ACTION, if (isButtonPilihPembayaran) EventAction.CLICK_PILIH_PEMBAYARAN else EventAction.CLICK_BAYAR,
                Key.EVENT_LABEL, "success",
                Key.E_COMMERCE, ee,
                Key.PAYMENT_ID, paymentId,
                ExtraKey.USER_ID, userId,
                ExtraKey.PAYMENT_TYPE, paymentType
        )
        sendEnhancedEcommerce(dataLayer)
    }

    fun eventClickInfoOnOSPNewBuyer() {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CLICK_BUTTON_INFO_ON_OSP,
                EventLabel.NEW_BUYER
        )
    }

    fun eventViewOnboardingInfo() {
        sendEventCategoryAction(
                EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
                EventCategory.ORDER_SUMMARY,
                EventAction.VIEW_ONBOARDING_INFO
        )
    }

    fun eventClickPromoOSP(promoCodes: List<String>) {
        if (promoCodes.isEmpty()) {
            sendEventCategoryAction(
                    EventName.CLICK_CHECKOUT_EXPRESS,
                    EventCategory.ORDER_SUMMARY,
                    EventAction.CLICK_PROMO_SECTION_NOT_APPLIED_OSP
            )
        } else {
            sendEventCategoryActionLabel(
                    EventName.CLICK_CHECKOUT_EXPRESS,
                    EventCategory.ORDER_SUMMARY,
                    EventAction.CLICK_PROMO_SECTION_APPLIED_OSP,
                    promoCodes.joinToString("-")
            )
        }
    }

    fun eventViewBottomSheetPromoError() {
        sendEventCategoryAction(
                EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
                EventCategory.EXPRESS_CHECKOUT,
                EventAction.VIEW_BOTTOMSHEET_PROMO_ERROR
        )
    }

    fun eventClickLanjutBayarPromoErrorOSP() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CLICK_LANJUT_BAYAR_PROMO_ERROR_OSP
        )
    }

    fun eventClickPilihPromoLainPromoErrorOSP() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CLICK_PILIH_PROMO_LAIN_PROMO_ERROR_OSP
        )
    }

    fun eventViewPromoAlreadyApplied() {
        sendEventCategoryAction(
                EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
                EventCategory.ORDER_SUMMARY,
                EventAction.VIEW_PROMO_ALREADY_APPLIED
        )
    }

    fun eventViewPromoDecreasedOrReleased(isReleased: Boolean) {
        sendEventCategoryAction(
                EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
                EventCategory.ORDER_SUMMARY,
                if (isReleased) EventAction.VIEW_PROMO_RELEASED else EventAction.VIEW_PROMO_DECREASED
        )
    }

    fun eventViewErrorMessage(error: String) {
        sendEventCategoryActionLabel(
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

    fun eventViewCoachmark1ForExistingUserOneProfile(userId: String) {
        val gtmData = getGtmData(
                EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
                EventCategory.ORDER_SUMMARY,
                EventAction.VIEW_COACHMARK_1_FOR_EXISTING_USER_ONE_PROFILE,
                ""
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventViewCoachmark2ForExistingUserOneProfile(userId: String) {
        val gtmData = getGtmData(
                EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
                EventCategory.ORDER_SUMMARY,
                EventAction.VIEW_COACHMARK_2_FOR_EXISTING_USER_ONE_PROFILE,
                ""
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventClickDoneOnCoachmark2ForExistingUserOneProfile(userId: String) {
        val gtmData = getGtmData(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CLICK_DONE_ON_COACHMARK_2_FOR_EXISTING_USER_ONE_PROFILE,
                ""
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventViewCoachmark1ForExistingUserMultiProfile(userId: String) {
        val gtmData = getGtmData(
                EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
                EventCategory.ORDER_SUMMARY,
                EventAction.VIEW_COACHMARK_1_FOR_EXISTING_USER_MULTI_PROFILE,
                ""
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventViewCoachmark2ForExistingUserMultiProfile(userId: String) {
        val gtmData = getGtmData(
                EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
                EventCategory.ORDER_SUMMARY,
                EventAction.VIEW_COACHMARK_2_FOR_EXISTING_USER_MULTI_PROFILE,
                ""
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventClickDoneOnCoachmark2ForExistingUserMultiProfile(userId: String) {
        val gtmData = getGtmData(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CLICK_DONE_ON_COACHMARK_2_FOR_EXISTING_USER_MULTI_PROFILE,
                ""
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventViewCoachmark1ForNewBuyerBeforeCreateProfile(userId: String) {
        val gtmData = getGtmData(
                EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
                EventCategory.ORDER_SUMMARY,
                EventAction.VIEW_COACHMARK_1_FOR_NEW_BUYER_BEFORE_CREATE_PROFILE,
                ""
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventViewCoachmark2ForNewBuyerBeforeCreateProfile(userId: String) {
        val gtmData = getGtmData(
                EventName.VIEW_CHECKOUT_EXPRESS_IRIS,
                EventCategory.ORDER_SUMMARY,
                EventAction.VIEW_COACHMARK_2_FOR_NEW_BUYER_BEFORE_CREATE_PROFILE,
                ""
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventClickLanjutOnCoachmark2ForNewBuyerBeforeCreateProfile(userId: String) {
        val gtmData = getGtmData(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.ORDER_SUMMARY,
                EventAction.CLICK_LANJUT_ON_COACHMARK_2_FOR_NEW_BUYER_BEFORE_CREATE_PROFILE,
                ""
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

    fun eventPPImpressionOnInsuranceSection(userId: String, categoryLvl3Id: String, insuranceBrand: String, protectionName: String) {
        val gtmData = getGtmData(
                EventName.PROMO_VIEW,
                EventCategory.ORDER_SUMMARY,
                EventAction.PP_IMPRESSION_ON_INSURANCE_SECTION,
                "$categoryLvl3Id - $insuranceBrand - $protectionName"
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventPPClickTooltip(userId: String, categoryLvl3Id: String, insuranceBrand: String, protectionName: String) {
        val gtmData = getGtmData(
                EventName.PROMO_CLICK,
                EventCategory.ORDER_SUMMARY,
                EventAction.PP_CLICK_TOOLTIP,
                "$categoryLvl3Id - $insuranceBrand - $protectionName"
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventPPClickBayar(userId: String, categoryLvl3Id: String, insuranceBrand: String, protectionName: String, isChecked: Boolean, ee: Map<String, Any>) {
        val dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.CHECKOUT,
                Key.EVENT_CATEGORY, EventCategory.ORDER_SUMMARY,
                Key.EVENT_ACTION, EventAction.PP_CLICK_BAYAR,
                Key.EVENT_LABEL, "$categoryLvl3Id - $insuranceBrand - $protectionName - $isChecked",
                Key.E_COMMERCE, ee,
                ExtraKey.USER_ID, userId,
                ExtraKey.BUSINESS_UNIT, CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM,
                ExtraKey.CURRENT_SITE, CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        )
        sendEnhancedEcommerce(dataLayer)
    }

    companion object {
        private const val NOT_SUCCESS = "not success"

        const val ERROR_ID_STOCK = "1"
        const val ERROR_ID_SHOP_CLOSED = "2"
        const val ERROR_ID_PRICE_CHANGE = "8"
        const val ERROR_ID_LOGISTIC_DURATION_UNAVAILABLE = "9"
        const val ERROR_ID_LOGISTIC_DISTANCE_EXCEED = "10"
        const val ERROR_ID_LOGISTIC_WEIGHT_EXCEED = "11"
        const val ERROR_ID_LOGISTIC_BBO_MINIMUM = "12"
        const val ERROR_ID_PAYMENT_OVO_BALANCE = "13"
    }
}