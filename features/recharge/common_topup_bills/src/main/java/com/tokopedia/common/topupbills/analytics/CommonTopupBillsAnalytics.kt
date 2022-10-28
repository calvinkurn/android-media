package com.tokopedia.common.topupbills.analytics

import android.os.Bundle
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.common.topupbills.analytics.CommonSmartBillsConstant.INDEX
import com.tokopedia.common.topupbills.analytics.CommonSmartBillsConstant.ITEMS
import com.tokopedia.common.topupbills.analytics.CommonSmartBillsConstant.ITEM_BRAND
import com.tokopedia.common.topupbills.analytics.CommonSmartBillsConstant.ITEM_CATEGORY
import com.tokopedia.common.topupbills.analytics.CommonSmartBillsConstant.ITEM_ID
import com.tokopedia.common.topupbills.analytics.CommonSmartBillsConstant.ITEM_LIST
import com.tokopedia.common.topupbills.analytics.CommonSmartBillsConstant.ITEM_NAME
import com.tokopedia.common.topupbills.analytics.CommonSmartBillsConstant.ITEM_VARIANT
import com.tokopedia.common.topupbills.analytics.CommonSmartBillsConstant.PRICE
import com.tokopedia.common.topupbills.analytics.CommonSmartBillsConstant.addGeneralClick
import com.tokopedia.common.topupbills.analytics.CommonSmartBillsConstant.addGeneralClickAddBills
import com.tokopedia.common.topupbills.analytics.CommonSmartBillsConstant.addGeneralViewAddBills
import com.tokopedia.common.topupbills.analytics.CommonSmartBillsConstant.addListBottomSheet
import com.tokopedia.common.topupbills.analytics.CommonTopupBillsEventTracking.*
import com.tokopedia.common.topupbills.analytics.CommonTopupBillsEventTracking.EnhanceEccomerce.Companion.ECOMMERCE
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst
import com.tokopedia.common.topupbills.data.RechargeAddBillsProductTrackData
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

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
                             isPromoUsed: Boolean = false,
                             isSpecialProduct: Boolean = false) {
        val isInstantCheckoutValue = if (isInstantCheckout) Label.INSTANT else Label.NO_INSTANT
        val isPromoUsedValue = if (isPromoUsed) Label.PROMO else Label.NO_PROMO

        // Click Buy
        val clickBuyEnhanceEccomerce = with(EnhanceEccomerce) {
            DataLayer.mapOf(
                    CURRENCY_CODE, DEFAULT_CURRENCY_CODE,
                    ADD,
                    DataLayer.mapOf(PRODUCTS,
                            DataLayer.listOf(
                                    DataLayer.mapOf(
                                            NAME, productName,
                                            ID, productId,
                                            PRICE, productPrice,
                                            BRAND, operatorName,
                                            CATEGORY, categoryName,
                                            VARIANT, if (isSpecialProduct) SPECIAL_PROMO else REGULAR_PRODUCT,
                                            QUANTITY, 1)
                            )
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
                    CHECKOUT,
                    DataLayer.mapOf(ActionField.ACTION_FIELD,
                            DataLayer.mapOf(ActionField.ACTION_FIELD_STEP, 1,
                                    ActionField.ACTION_FIELD_OPTION, ActionField.ACTION_FIELD_VIEW_CHECKOUT
                            ),
                            PRODUCTS,
                            DataLayer.listOf(
                                    DataLayer.mapOf(NAME, productName,
                                            ID, productId,
                                            PRICE, productPrice,
                                            BRAND, operatorName,
                                            CATEGORY, categoryName,
                                            VARIANT, if (isSpecialProduct) SPECIAL_PROMO else REGULAR_PRODUCT,
                                            QUANTITY, 1
                                    )
                            )
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
                    CHECKOUT,
                    DataLayer.mapOf(ActionField.ACTION_FIELD,
                            DataLayer.mapOf(ActionField.ACTION_FIELD_STEP, 2,
                                    ActionField.ACTION_FIELD_OPTION, ActionField.ACTION_FIELD_CLICK_CHECKOUT
                            ),
                            PRODUCTS,
                            DataLayer.listOf(DataLayer.mapOf(NAME, productName,
                                    ID, productId,
                                    PRICE, productPrice,
                                    BRAND, operatorName,
                                    CATEGORY, categoryName,
                                    VARIANT, if (isSpecialProduct) SPECIAL_PROMO else REGULAR_PRODUCT,
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

    //Add Bills
    //#5
    fun clickBackTelcoAddBills(category: String) {
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, "click back",
                TrackAppUtils.EVENT_LABEL, category)
        data.addGeneralClick()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    //#6
    fun clickCloseTickerTelcoAddBills(category: String) {
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, "click x - top information box",
                TrackAppUtils.EVENT_LABEL, category)

        data.addGeneralClickAddBills()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    //#7
    fun clickInputFieldTelcoAddBills(category: String) {
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, "click input field",
                TrackAppUtils.EVENT_LABEL, category)
        data.addGeneralClickAddBills()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    //#8
    fun clickDropDownListTelcoAddBills(category: String, dropdownName: String, position: String) {
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, String.format("%s %s", "click drop down list ", position),
                TrackAppUtils.EVENT_LABEL, String.format("%s - %s", category, dropdownName)
        )
        data.addGeneralClickAddBills()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    //#9
    fun clickCloseDropDownListTelcoAddBills(category: String, dropdownName: String) {
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, "click x - drop down list product",
                TrackAppUtils.EVENT_LABEL, String.format("%s - %s", category, dropdownName)
        )
        data.addGeneralClickAddBills()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    //#10
    fun viewBottomSheetAddBills(userId:String, categoryName: String, dropdownName: String,
                                products: List<RechargeAddBillsProductTrackData>){
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, "click product")
            putString(TrackAppUtils.EVENT_LABEL, String.format("%s - %s", categoryName, dropdownName))
            putString(ITEM_LIST, "")
            putParcelableArrayList(ITEMS, getProductFromMetaData(products))
        }

        eventDataLayer.addListBottomSheet(userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(CommonSmartBillsConstant.SELECT_CONTENT, eventDataLayer)
    }

    //#12
    fun clickTambahTagihanTelcoAddBills(category: String) {
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, "click tambah tagihan / lanjut",
                TrackAppUtils.EVENT_LABEL, category)
        data.addGeneralClickAddBills()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    //#13
    fun clickViewErrorToasterTelcoAddBills(category: String, errorMessage: String) {
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, "view error - toaster box",
                TrackAppUtils.EVENT_LABEL, String.format("%s - %s", category, errorMessage))
        data.addGeneralViewAddBills()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    //#21
    fun clickOnCloseInquiry(category: String){
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, "click x detail tagihan",
                TrackAppUtils.EVENT_LABEL, category)
        data.addGeneralClick()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    //#22
    fun clickAddInquiry(category: String){
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, "click tambah tagihan ini",
                TrackAppUtils.EVENT_LABEL, category)
        data.addGeneralClick()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    private fun getProductFromMetaData(products: List<RechargeAddBillsProductTrackData>): ArrayList<Bundle>{
        val list = arrayListOf<Bundle>()
        products.forEach { it ->
            val itemBundle = Bundle().apply {
                putString(INDEX, it.index.toString())
                putString(ITEM_BRAND, it.itemBrand)
                putString(ITEM_CATEGORY, it.itemCategory)
                putString(ITEM_ID, it.itemId)
                putString(ITEM_NAME, it.itemName)
                putString(ITEM_VARIANT, it.itemVariant)
                putString(PRICE, it.price.toString())
            }
            list.add(itemBundle)
        }
        return list
    }

    fun eventImpressionFavoriteNumberEmptyState(categoryName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.DIGITAL_GENERAL_EVENT_IRIS,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                        TrackAppUtils.EVENT_ACTION, Action.FAVNUMBER_VIEW_EMPTY_FAVORITE_NUMBER,
                        TrackAppUtils.EVENT_LABEL, categoryName,
                        DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
                        DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE,
                        DigitalTrackingConst.Label.USER_ID, userId
                )
        )
    }

    fun eventImpressionFavoriteNumberCoachmark(categoryName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.DIGITAL_GENERAL_EVENT_IRIS,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                        TrackAppUtils.EVENT_ACTION, Action.FAVNUMBER_VIEW_COACHMARK,
                        TrackAppUtils.EVENT_LABEL, categoryName,
                        DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
                        DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE,
                        DigitalTrackingConst.Label.USER_ID, userId
                )
        )
    }

    fun eventClickFavoriteNumberContinue(categoryName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.DIGITAL_GENERAL_EVENT,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                        TrackAppUtils.EVENT_ACTION, Action.FAVNUMBER_CLICK_CONTINUE,
                        TrackAppUtils.EVENT_LABEL, categoryName,
                        DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
                        DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE,
                        DigitalTrackingConst.Label.USER_ID, userId
                )
        )
    }

    fun eventClickFavoriteNumberKebabMenu(categoryName: String, operatorName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.DIGITAL_GENERAL_EVENT,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                        TrackAppUtils.EVENT_ACTION, Action.FAVNUMBER_CLICK_KEBAB_MENU,
                        TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName",
                        DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
                        DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE,
                        DigitalTrackingConst.Label.USER_ID, userId
                )
        )
    }

    fun eventImpressionEditBottomSheet(categoryName: String, operatorName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.DIGITAL_GENERAL_EVENT_IRIS,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                        TrackAppUtils.EVENT_ACTION, Action.FAVNUMBER_VIEW_EDIT_BOTTOM_SHEET,
                        TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName",
                        DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
                        DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE,
                        DigitalTrackingConst.Label.USER_ID, userId
                )
        )
    }

    fun eventClickFavoriteNumberSaveBottomSheet(categoryName: String, operatorName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.DIGITAL_GENERAL_EVENT,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                        TrackAppUtils.EVENT_ACTION, Action.FAVNUMBER_CLICK_SAVE_BOTTOM_SHEET,
                        TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName",
                        DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
                        DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE,
                        DigitalTrackingConst.Label.USER_ID, userId
                )
        )
    }

    fun eventImpressionFavoriteNumberDeletePopUp(categoryName: String, operatorName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.DIGITAL_GENERAL_EVENT_IRIS,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                        TrackAppUtils.EVENT_ACTION, Action.FAVNUMBER_VIEW_DELETION_POP_UP,
                        TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName",
                        DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
                        DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE,
                        DigitalTrackingConst.Label.USER_ID, userId
                )
        )
    }

    fun eventClickFavoriteNumberConfirmDelete(categoryName: String, operatorName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.DIGITAL_GENERAL_EVENT,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                        TrackAppUtils.EVENT_ACTION, Action.FAVNUMBER_CLICK_CONFIRM_DELETE_POP_UP,
                        TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName",
                        DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
                        DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE,
                        DigitalTrackingConst.Label.USER_ID, userId
                )
        )
    }

    fun eventImpressionFavoriteNumberSuccessDeleteToaster(categoryName: String, operatorName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.DIGITAL_GENERAL_EVENT_IRIS,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                        TrackAppUtils.EVENT_ACTION, Action.FAVNUMBER_VIEW_DELETION_SUCCESS_TOASTER,
                        TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName",
                        DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
                        DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE,
                        DigitalTrackingConst.Label.USER_ID, userId
                )
        )
    }

    fun eventImpressionFavoriteNumberFailedDeleteToaster(categoryName: String, operatorName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.DIGITAL_GENERAL_EVENT_IRIS,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                        TrackAppUtils.EVENT_ACTION, Action.FAVNUMBER_VIEW_DELETION_FAILED_TOASTER,
                        TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName",
                        DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
                        DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE,
                        DigitalTrackingConst.Label.USER_ID, userId
                )
        )
    }

    fun eventImpressionTotalFavoriteNumber(totalUnnamedFavNumber: Int, totalNamedFavNumber: Int, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, Event.DIGITAL_GENERAL_EVENT_IRIS,
                TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                TrackAppUtils.EVENT_ACTION, Action.FAVNUMBER_IMPRESSION_FAV_NUMBER_AND_CONTACT,
                TrackAppUtils.EVENT_LABEL, "$totalUnnamedFavNumber - $totalNamedFavNumber",
                DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
                DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE,
                DigitalTrackingConst.Label.USER_ID, userId
            )
        )
    }

    fun eventClickTotalFavoriteNumber(totalUnnamedFavNumber: Int, totalNamedFavNumber: Int, clickPosition: Int, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, Event.DIGITAL_GENERAL_EVENT,
                TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                TrackAppUtils.EVENT_ACTION, Action.FAVNUMBER_CLICK_FAV_NUMBER_AND_CONTACT,
                TrackAppUtils.EVENT_LABEL, "$totalUnnamedFavNumber - $totalNamedFavNumber - $clickPosition",
                DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
                DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE,
                DigitalTrackingConst.Label.USER_ID, userId
            )
        )
    }

    fun eventClickMenuFavoriteNumberDelete(categoryName: String, operatorName: String, loyaltyStatus: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, Event.CLICK_DIGITAL,
                TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                TrackAppUtils.EVENT_ACTION, Action.FAVNUMBER_CLICK_DELETE_ON_KEBAB_MENU,
                TrackAppUtils.EVENT_LABEL, "${categoryName}_${operatorName}_${loyaltyStatus}",
                DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
                DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE,
                DigitalTrackingConst.Label.USER_ID, userId
            )
        )
    }

    fun eventClickMenuFavoriteNumberModify(categoryName: String, operatorName: String, loyaltyStatus: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, Event.CLICK_DIGITAL,
                TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                TrackAppUtils.EVENT_ACTION, Action.FAVNUMBER_CLICK_UBAH_NAMA_ON_KEBAB_MENU,
                TrackAppUtils.EVENT_LABEL, "${categoryName}_${operatorName}_${loyaltyStatus}",
                DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
                DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE,
                DigitalTrackingConst.Label.USER_ID, userId
            )
        )
    }

    fun eventClickMenuCancelFavoriteNumberModify(categoryName: String, operatorName: String, loyaltyStatus: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, Event.CLICK_DIGITAL,
                TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_PDP_FAVORITE_NUMBER,
                TrackAppUtils.EVENT_ACTION, Action.FAVNUMBER_CLICK_X_ON_KEBAB_MENU,
                TrackAppUtils.EVENT_LABEL, "${categoryName}_${operatorName}_${loyaltyStatus}",
                DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
                DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE,
                DigitalTrackingConst.Label.USER_ID, userId
            )
        )
    }
}