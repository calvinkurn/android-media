package com.tokopedia.common.topupbills.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.common.topupbills.analytics.CommonTopupBillsEventTracking.*
import com.tokopedia.common.topupbills.analytics.CommonTopupBillsEventTracking.EnhanceEccomerce.Companion.ECOMMERCE
import com.tokopedia.common.topupbills.data.constant.RechargeCategory
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import java.util.*

class CommonTopupBillsAnalytics {
    fun eventClickUsePromo() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_NATIVE,
                Action.CLICK_USE_PROMO,
                ""
        )
    }

    fun eventClickRemovePromo() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_NATIVE,
                Action.CLICK_REMOVE_PROMO,
                ""
        )
    }

    fun eventExpressCheckout(categoryName: String,
                             operatorName: String,
                             productId: String,
                             productName: String,
                             productPrice: Int,
                             isInstantCheckout: Boolean = false,
                             isPromoUsed: Boolean = false) {
        val isInstantCheckoutValue = if (isInstantCheckout) Label.INSTANT else Label.NO_INSTANT
        val isPromoUsedValue = if (isPromoUsed) Label.PROMO else Label.NO_PROMO

        // Click Buy
        val clickBuyEnhanceEccomerce = with(EnhanceEccomerce) {
            DataLayer.mapOf(
                    CURRENCY_CODE, DEFAULT_CURRENCY_CODE,
                    ADD, DataLayer.mapOf(
                        PRODUCTS, DataLayer.listOf(DataLayer.mapOf(
                            NAME, productName,
                            ID, productId,
                            PRICE, productPrice,
                            BRAND, operatorName,
                            CATEGORY, categoryName,
                            VARIANT, EMPTY,
                            QUANTITY, 1
                        ))
                    )
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.ADD_TO_CART,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_HOMEPAGE,
                        TrackAppUtils.EVENT_ACTION, Action.CLICK_BUY,
                        TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName - $isInstantCheckoutValue",
                        ECOMMERCE, clickBuyEnhanceEccomerce
                )
        )

        // View Checkout
        val viewCheckoutEnhanceEccomerce = with(EnhanceEccomerce) {
            DataLayer.mapOf(
                    CHECKOUT, DataLayer.mapOf(
                        ActionField.ACTION_FIELD, DataLayer.mapOf(
                            ActionField.ACTION_FIELD_STEP, 1,
                            ActionField.ACTION_FIELD_OPTION, ActionField.ACTION_FIELD_VIEW_CHECKOUT
                        ),
                        PRODUCTS, DataLayer.listOf(DataLayer.mapOf(
                            NAME, productName,
                            ID, productId,
                            PRICE, productPrice,
                            BRAND, operatorName,
                            CATEGORY, categoryName,
                            VARIANT, EMPTY,
                            QUANTITY, 1
                        ))
                    )
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.CHECKOUT,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_CHECKOUT,
                        TrackAppUtils.EVENT_ACTION, Action.VIEW_CHECKOUT,
                        TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName",
                        ECOMMERCE, viewCheckoutEnhanceEccomerce
                )
        )

        // Click Proceed to Payment
        val clickPaymentEnhanceEccomerce = with(EnhanceEccomerce) {
            DataLayer.mapOf(
                    CHECKOUT, DataLayer.mapOf(
                        ActionField.ACTION_FIELD, DataLayer.mapOf(
                            ActionField.ACTION_FIELD_STEP, 2,
                            ActionField.ACTION_FIELD_OPTION, ActionField.ACTION_FIELD_CLICK_CHECKOUT
                        ),
                        PRODUCTS, DataLayer.listOf(DataLayer.mapOf(
                            NAME, productName,
                            ID, productId,
                            PRICE, productPrice,
                            BRAND, operatorName,
                            CATEGORY, categoryName,
                            VARIANT, EMPTY,
                            QUANTITY, 1
                        ))
                    )
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.CHECKOUT,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_CHECKOUT,
                        TrackAppUtils.EVENT_ACTION, Action.CLICK_PROCEED_TO_PAYMENT,
                        TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName - $isPromoUsedValue",
                        ECOMMERCE, clickPaymentEnhanceEccomerce
                )
        )
    }

    fun eventImpressionFavoriteNumberEmptyState(categoryName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.DIGITAL_GENERAL_EVENT_IRIS,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                        TrackAppUtils.EVENT_ACTION, Action.VIEW_EMPTY_FAVORITE_NUMBER,
                        TrackAppUtils.EVENT_LABEL, categoryName,
                        General.Key.BUSINESS_UNIT, General.Value.BUSINESS_UNIT_RECHARGE,
                        General.Key.CURRENT_SITE, General.Value.CURRENT_SITE_RECHARGE,
                        General.Key.USER_ID, userId
                )
        )
    }

    fun eventImpressionFavoriteNumberCoachmark(categoryName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.DIGITAL_GENERAL_EVENT_IRIS,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                        TrackAppUtils.EVENT_ACTION, Action.VIEW_COACHMARK,
                        TrackAppUtils.EVENT_LABEL, categoryName,
                        General.Key.BUSINESS_UNIT, General.Value.BUSINESS_UNIT_RECHARGE,
                        General.Key.CURRENT_SITE, General.Value.CURRENT_SITE_RECHARGE,
                        General.Key.USER_ID, userId
                )
        )
    }

    fun eventClickFavoriteNumberContinue(categoryName: String, operatorName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.DIGITAL_GENERAL_EVENT,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                        TrackAppUtils.EVENT_ACTION, Action.CLICK_CONTINUE,
                        TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName",
                        General.Key.BUSINESS_UNIT, General.Value.BUSINESS_UNIT_RECHARGE,
                        General.Key.CURRENT_SITE, General.Value.CURRENT_SITE_RECHARGE,
                        General.Key.USER_ID, userId
                )
        )
    }

    fun eventClickFavoriteNumberKebabMenu(categoryName: String, operatorName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.DIGITAL_GENERAL_EVENT,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                        TrackAppUtils.EVENT_ACTION, Action.CLICK_KEBAB_MENU,
                        TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName",
                        General.Key.BUSINESS_UNIT, General.Value.BUSINESS_UNIT_RECHARGE,
                        General.Key.CURRENT_SITE, General.Value.CURRENT_SITE_RECHARGE,
                        General.Key.USER_ID, userId
                )
        )
    }

    fun eventImpressionEditBottomSheet(categoryName: String, operatorName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.DIGITAL_GENERAL_EVENT_IRIS,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                        TrackAppUtils.EVENT_ACTION, Action.VIEW_EDIT_BOTTOM_SHEET,
                        TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName",
                        General.Key.BUSINESS_UNIT, General.Value.BUSINESS_UNIT_RECHARGE,
                        General.Key.CURRENT_SITE, General.Value.CURRENT_SITE_RECHARGE,
                        General.Key.USER_ID, userId
                )
        )
    }

    fun eventClickFavoriteNumberSaveBottomSheet(categoryName: String, operatorName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.DIGITAL_GENERAL_EVENT,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                        TrackAppUtils.EVENT_ACTION, Action.CLICK_SAVE_BOTTOM_SHEET,
                        TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName",
                        General.Key.BUSINESS_UNIT, General.Value.BUSINESS_UNIT_RECHARGE,
                        General.Key.CURRENT_SITE, General.Value.CURRENT_SITE_RECHARGE,
                        General.Key.USER_ID, userId
                )
        )
    }

    fun eventImpressionFavoriteNumberDeletePopUp(categoryName: String, operatorName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.DIGITAL_GENERAL_EVENT_IRIS,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                        TrackAppUtils.EVENT_ACTION, Action.VIEW_DELETION_POP_UP,
                        TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName",
                        General.Key.BUSINESS_UNIT, General.Value.BUSINESS_UNIT_RECHARGE,
                        General.Key.CURRENT_SITE, General.Value.CURRENT_SITE_RECHARGE,
                        General.Key.USER_ID, userId
                )
        )
    }

    fun eventClickFavoriteNumberConfirmDelete(categoryName: String, operatorName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.DIGITAL_GENERAL_EVENT,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                        TrackAppUtils.EVENT_ACTION, Action.CLICK_CONFIRM_DELETE_POP_UP,
                        TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName",
                        General.Key.BUSINESS_UNIT, General.Value.BUSINESS_UNIT_RECHARGE,
                        General.Key.CURRENT_SITE, General.Value.CURRENT_SITE_RECHARGE,
                        General.Key.USER_ID, userId
                )
        )
    }

    fun eventImpressionFavoriteNumberSuccessDeleteToaster(categoryName: String, operatorName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.DIGITAL_GENERAL_EVENT_IRIS,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                        TrackAppUtils.EVENT_ACTION, Action.VIEW_DELETION_SUCCESS_TOASTER,
                        TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName",
                        General.Key.BUSINESS_UNIT, General.Value.BUSINESS_UNIT_RECHARGE,
                        General.Key.CURRENT_SITE, General.Value.CURRENT_SITE_RECHARGE,
                        General.Key.USER_ID, userId
                )
        )
    }

    fun eventImpressionFavoriteNumberFailedDeleteToaster(categoryName: String, operatorName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.DIGITAL_GENERAL_EVENT_IRIS,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                        TrackAppUtils.EVENT_ACTION, Action.VIEW_DELETION_FAILED_TOASTER,
                        TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName",
                        General.Key.BUSINESS_UNIT, General.Value.BUSINESS_UNIT_RECHARGE,
                        General.Key.CURRENT_SITE, General.Value.CURRENT_SITE_RECHARGE,
                        General.Key.USER_ID, userId
                )
        )
    }
}