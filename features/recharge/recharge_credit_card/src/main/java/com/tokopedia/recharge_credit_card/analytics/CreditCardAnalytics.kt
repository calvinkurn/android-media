package com.tokopedia.recharge_credit_card.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst
import com.tokopedia.iris.Iris
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.Tracker
import com.tokopedia.track.builder.util.BaseTrackerConst
import java.util.*

class CreditCardAnalytics(val iris: Iris) {

    fun openCCScreen() {
        val customDimension: MutableMap<String, String> = HashMap()
        customDimension[DigitalTrackingConst.Label.BUSINESS_UNIT] = DigitalTrackingConst.Value.RECHARGE_BU
        customDimension[DigitalTrackingConst.Label.CURRENTSITE] = DigitalTrackingConst.Value.RECHARGE_SITE
        TrackApp.getInstance().gtm.sendScreenAuthenticated(CC_SCREEN_NAME, customDimension)
    }

    fun impressionInitialPage(userId: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_VIEW_DIGITAL_IRIS,
            CATEGORY_HOMEPAGE,
            ACTION_IMPRESSION_INITIAL,
            getCategoryName()
        )
        map[KEY_USER_ID] = userId
        map[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_RECHARGE
        map[KEY_CURRENT_SITE] = CURRENT_SITE_RECHARGE
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun impressionBankList(categoryId: String, operatorId: String, userId: String) {
        if (iris != null) {
            val map = TrackAppUtils.gtmData(
                EVENT_VIEW_DIGITAL_CC_IRIS,
                CATEGORY_CC,
                ACTION_IMPRESSION_BANKLIST,
                "$categoryId - $operatorId"
            )
            map[KEY_USER_ID] = userId
            iris.saveEvent(map)
        }
    }

    fun clickToConfirmationPage(categoryId: String, operatorId: String, userId: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_CLICK_DIGITAL_CC,
            CATEGORY_CC,
            ACTION_TO_SHOW_CONFIRMATION,
            "$categoryId - $operatorId"
        )
        map[KEY_USER_ID] = userId
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickBackOnConfirmationPage(categoryId: String, operatorId: String, userId: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_CLICK_DIGITAL_CC,
            CATEGORY_CC,
            ACTION_CLICK_BACK_CONFIRMATION,
            "$categoryId - $operatorId"
        )
        map[KEY_USER_ID] = userId
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickToContinueCheckout(categoryId: String, operatorId: String, userId: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_CLICK_DIGITAL_CC,
            CATEGORY_CC,
            ACTION_CLICK_CHECKOUT,
            "$categoryId - $operatorId"
        )
        map[KEY_USER_ID] = userId
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun addToCart(userId: String, categoryName: String, categoryId: String, prefixName: String, prefixId: String) {
        val products: MutableList<Any> = ArrayList()
        products.add(constructProductEnhanceEcommerce(prefixName, prefixId, categoryName, categoryId))
        val defaultChannelId = 1

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalTrackingConst.Event.ADD_TO_CART,
                TrackAppUtils.EVENT_CATEGORY, DigitalTrackingConst.Category.DIGITAL_NATIVE,
                TrackAppUtils.EVENT_ACTION, DigitalTrackingConst.Action.CLICK_BELI,
                TrackAppUtils.EVENT_LABEL, "$categoryName - $prefixName - $defaultChannelId",
                DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
                DigitalTrackingConst.Label.USER_ID, userId,
                BaseTrackerConst.Ecommerce.KEY,
                DataLayer.mapOf(
                    DigitalTrackingConst.CurrencyCode.KEY,
                    DigitalTrackingConst.CurrencyCode.IDR,
                    DigitalTrackingConst.Label.ADD,
                    DataLayer.mapOf(DigitalTrackingConst.Label.PRODUCTS, DataLayer.listOf(*products.toTypedArray()))
                ),
                DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE
            )
        )
    }

    fun impressionFavoriteNumberChips(categoryName: String, loyaltyStatus: String, userId: String) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, ACTION_VIEW_FAVORITE_NUMBER_CHIP,
            TrackAppUtils.EVENT_LABEL, "${categoryName}_$loyaltyStatus",
            TrackAppUtils.EVENT_CATEGORY, DigitalTrackingConst.Category.DIGITAL_HOMEPAGE,
            TrackAppUtils.EVENT, EVENT_VIEW_DIGITAL_IRIS,
            DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE,
            DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
            DigitalTrackingConst.Label.USER_ID, userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun clickFavoriteNumberChips(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, ACTION_CLICK_FAVORITE_NUMBER_CHIP,
            TrackAppUtils.EVENT_LABEL, "${categoryName}_${operatorName}_$loyaltyStatus",
            TrackAppUtils.EVENT_CATEGORY, DigitalTrackingConst.Category.DIGITAL_HOMEPAGE,
            TrackAppUtils.EVENT, EVENT_CLICK_DIGITAL,
            DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE,
            DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
            DigitalTrackingConst.Label.USER_ID, userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun impressionFavoriteContactChips(
        categoryName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, ACTION_VIEW_FAVORITE_CONTACT_CHIP,
            TrackAppUtils.EVENT_LABEL, "${categoryName}_$loyaltyStatus",
            TrackAppUtils.EVENT_CATEGORY, DigitalTrackingConst.Category.DIGITAL_HOMEPAGE,
            TrackAppUtils.EVENT, EVENT_VIEW_DIGITAL_IRIS,
            DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE,
            DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
            DigitalTrackingConst.Label.USER_ID, userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun clickFavoriteContactChips(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, ACTION_CLICK_FAVORITE_CONTACT_CHIP,
            TrackAppUtils.EVENT_LABEL, "${categoryName}_${operatorName}_$loyaltyStatus",
            TrackAppUtils.EVENT_CATEGORY, DigitalTrackingConst.Category.DIGITAL_HOMEPAGE,
            TrackAppUtils.EVENT, EVENT_CLICK_DIGITAL,
            DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE,
            DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
            DigitalTrackingConst.Label.USER_ID, userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun clickFavoriteNumberAutoComplete(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, ACTION_CLICK_AUTOCOMPLETE_FAVORITE_NUMBER,
            TrackAppUtils.EVENT_LABEL, "${categoryName}_${operatorName}_$loyaltyStatus",
            TrackAppUtils.EVENT_CATEGORY, DigitalTrackingConst.Category.DIGITAL_HOMEPAGE,
            TrackAppUtils.EVENT, EVENT_CLICK_DIGITAL,
            DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE,
            DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
            DigitalTrackingConst.Label.USER_ID, userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun clickFavoriteContactAutoComplete(
        categoryName: String,
        operatorName: String,
        loyaltyStatus: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, ACTION_CLICK_AUTOCOMPLETE_FAVORITE_CONTACT,
            TrackAppUtils.EVENT_LABEL, "${categoryName}_${operatorName}_$loyaltyStatus",
            TrackAppUtils.EVENT_CATEGORY, DigitalTrackingConst.Category.DIGITAL_HOMEPAGE,
            TrackAppUtils.EVENT, EVENT_CLICK_DIGITAL,
            DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE,
            DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
            DigitalTrackingConst.Label.USER_ID, userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3815
    // Tracker ID: 42036
    fun sendClickLastTransactionTabEvent(categoryName: String, loyaltyStatus: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_DIGITAL)
            .setEventAction(ACTION_CLICK_LAST_TRANSACTION_TAB)
            .setEventCategory(DigitalTrackingConst.Category.DIGITAL_HOMEPAGE)
            .setEventLabel("$categoryName - $loyaltyStatus")
            .setCustomProperty(DigitalTrackingConst.Other.KEY_TRACKER_ID, CreditCardConst.TrackerId.CLICK_LAST_TRANSACTION_TAB)
            .setBusinessUnit(DigitalTrackingConst.Value.RECHARGE_BU)
            .setCurrentSite(DigitalTrackingConst.Value.RECHARGE_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3815
    // Tracker ID: 42037
    fun sendClickLastTransactionListEvent(categoryName: String, loyaltyStatus: String, position: Int) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_DIGITAL)
            .setEventAction(ACTION_CLICK_LAST_TRANSACTION_LIST)
            .setEventCategory(DigitalTrackingConst.Category.DIGITAL_HOMEPAGE)
            .setEventLabel("$categoryName - $loyaltyStatus - $position")
            .setCustomProperty(DigitalTrackingConst.Other.KEY_TRACKER_ID, CreditCardConst.TrackerId.CLICK_LAST_TRANSACTION_LIST)
            .setBusinessUnit(DigitalTrackingConst.Value.RECHARGE_BU)
            .setCurrentSite(DigitalTrackingConst.Value.RECHARGE_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3815
    // Tracker ID: 42038
    fun sendClickPromoTabEvent(categoryName: String, loyaltyStatus: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_DIGITAL)
            .setEventAction(ACTION_CLICK_PROMO_TAB)
            .setEventCategory(DigitalTrackingConst.Category.DIGITAL_HOMEPAGE)
            .setEventLabel("$categoryName - $loyaltyStatus")
            .setCustomProperty(DigitalTrackingConst.Other.KEY_TRACKER_ID, CreditCardConst.TrackerId.CLICK_PROMO_TAB)
            .setBusinessUnit(DigitalTrackingConst.Value.RECHARGE_BU)
            .setCurrentSite(DigitalTrackingConst.Value.RECHARGE_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3815
    // Tracker ID: 42569
    fun sendClickSalinPromoDigitalEvent(
        categoryName: String,
        loyaltyStatus: String,
        promoCode: String,
        position: Int
    ) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_DIGITAL)
            .setEventAction(ACTION_CLICK_SALIN_PROMO_DIGITAL)
            .setEventCategory(DigitalTrackingConst.Category.DIGITAL_HOMEPAGE)
            .setEventLabel("$categoryName - $loyaltyStatus - $promoCode - $position")
            .setCustomProperty(DigitalTrackingConst.Other.KEY_TRACKER_ID, CreditCardConst.TrackerId.CLICK_SALIN_PROMO_DIGITAL)
            .setBusinessUnit(DigitalTrackingConst.Value.RECHARGE_BU)
            .setCurrentSite(DigitalTrackingConst.Value.RECHARGE_SITE)
            .build()
            .send()
    }

    private fun constructProductEnhanceEcommerce(
        prefixName: String,
        prefixId: String,
        categoryName: String,
        categoryId: String
    ): Map<String?, Any?> {
        return DataLayer.mapOf(
            DigitalTrackingConst.Product.KEY_NAME, prefixName,
            DigitalTrackingConst.Product.KEY_ID, prefixId,
            DigitalTrackingConst.Product.KEY_PRICE, 0,
            DigitalTrackingConst.Product.KEY_BRAND, prefixName,
            DigitalTrackingConst.Product.KEY_CATEGORY, categoryName,
            DigitalTrackingConst.Product.KEY_VARIANT, DigitalTrackingConst.Value.NONE,
            DigitalTrackingConst.Product.KEY_QUANTITY, "1",
            DigitalTrackingConst.Product.KEY_CATEGORY_ID, categoryId,
            DigitalTrackingConst.Product.KEY_SHOP_ID, DigitalTrackingConst.Value.NONE,
            DigitalTrackingConst.Product.KEY_SHOP_NAME, DigitalTrackingConst.Value.NONE,
            DigitalTrackingConst.Product.KEY_SHOP_TYPE, DigitalTrackingConst.Value.NONE
        )
    }

    companion object {
        private fun getCategoryName(): String = "Tagihan Kartu Kredit"

        const val KEY_USER_ID = "userId"
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val KEY_CURRENT_SITE = "currentSite"

        const val CATEGORY_CC = "digital - cc page"
        const val CATEGORY_HOMEPAGE = "digital - homepage"

        const val EVENT_CLICK_DIGITAL_CC = "clickDigitalCC"
        const val EVENT_CLICK_DIGITAL = "clickDigital"
        const val EVENT_VIEW_DIGITAL_CC_IRIS = "viewDigitalCCIris"
        const val EVENT_VIEW_DIGITAL_IRIS = "viewDigitalIris"

        const val ACTION_IMPRESSION_INITIAL = "view pdp page"
        const val ACTION_IMPRESSION_BANKLIST = "impression of bank list"
        const val ACTION_CLICK_CHECKOUT = "checkout page"
        const val ACTION_CLICK_BACK_CONFIRMATION = "click confirmation to pdp"
        const val ACTION_TO_SHOW_CONFIRMATION = "click confirmation to checkout"
        const val ACTION_VIEW_FAVORITE_NUMBER_CHIP = "view favorite number chip"
        const val ACTION_VIEW_FAVORITE_CONTACT_CHIP = "view favorite contact chip"
        const val ACTION_CLICK_FAVORITE_NUMBER_CHIP = "click favorite number chip"
        const val ACTION_CLICK_FAVORITE_CONTACT_CHIP = "click favorite contact chip"
        const val ACTION_CLICK_AUTOCOMPLETE_FAVORITE_NUMBER = "click autocomplete fav number"
        const val ACTION_CLICK_AUTOCOMPLETE_FAVORITE_CONTACT = "click autocomplete fav contact"
        const val ACTION_CLICK_LAST_TRANSACTION_TAB = "click last transaction tab"
        const val ACTION_CLICK_LAST_TRANSACTION_LIST = "click last transaction list"
        const val ACTION_CLICK_PROMO_TAB = "click promo tab"
        const val ACTION_CLICK_SALIN_PROMO_DIGITAL = "click salin promo digital"

        const val BUSINESS_UNIT_RECHARGE = "recharge"
        const val CURRENT_SITE_RECHARGE = "tokopediadigitalRecharge"
        const val CC_SCREEN_NAME = "/digital/Kartu Kredit"
    }
}
