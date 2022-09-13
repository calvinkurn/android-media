package com.tokopedia.topupbills.common.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.common.topupbills.data.TopupBillsPromo
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.data.constant.GeneralCategoryType
import com.tokopedia.common.topupbills.data.constant.GeneralComponentName
import com.tokopedia.common.topupbills.data.constant.TelcoCategoryType
import com.tokopedia.common.topupbills.data.constant.TelcoComponentName
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackPromo
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackRecentTransaction
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils


/**
 * @author by resakemal on 05/07/19.
 */

class DigitalTopupAnalytics {

    fun eventInputNumberManual(categoryId: Int, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.INPUT_MANUAL_NUMBER,
                "${getTrackingCategoryName(categoryId)} - $operatorName"
        ))
    }

    fun eventInputNumberContactPicker(categoryId: Int, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.INPUT_FROM_CONTACT,
                "${getTrackingCategoryName(categoryId)} - $operatorName"
        ))
    }

    fun eventClickOnContactPickerHomepage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_ON_CONTACT,
                ""
        ))
    }

    fun eventInputNumberFavorites(categoryId: Int, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.INPUT_FROM_FAVORITE_NUMBER,
                "${getTrackingCategoryName(categoryId)} - $operatorName"
        ))
    }

    fun eventClearInputNumber() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLEAR_INPUT_NUMBER,
                ""
        ))
    }

    fun eventClickBackButton(categoryId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_BACK_BUTTON,
                getTrackingCategoryName(categoryId)
        ))
    }

    fun eventClickCopyPromoCode(promoName: String, position: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.COPY_PROMO_DIGITAL,
                "$promoName - $position"
        ))
    }

    fun eventClickCheckEnquiry(categoryId: Int, operatorName: String, userId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_CHECK_TAGIHAN,
                "${getTrackingCategoryName(categoryId)} - $operatorName")
        sendGeneralEvent(mapEvent, userId)
    }

    fun eventClickTabMenuTelco(categoryId: Int, userId: String, action: String) {
        val mapEvent = TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                action,
                getTrackingCategoryName(categoryId))
        sendGeneralEvent(mapEvent, userId)
    }

    fun eventClickDotsMenuTelco(categoryId: String, userId: String) {
        var category = getTrackingCategoryName(categoryId.toIntSafely())
        if (categoryId.isEmpty()) category = categoryId

        val mapEvent = TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_DOTS_MENU,
                category)
        sendGeneralEvent(mapEvent, userId)
    }

    private fun sendGeneralEvent(mapEvent: MutableMap<String, Any>, userId: String) {
        mapEvent[DigitalTopupEventTracking.Additional.BUSINESS_UNIT] =
                DigitalTopupEventTracking.Additional.BUSINESS_UNIT_RECHARGE
        mapEvent[DigitalTopupEventTracking.Additional.CURRENT_SITE] =
                DigitalTopupEventTracking.Additional.CURRENT_SITE_RECHARGE
        mapEvent[DigitalTopupEventTracking.Additional.USER_ID] = userId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun impressionEnhanceCommerceRecentTransaction(topupBillsTrackRecent: List<TopupBillsTrackRecentTransaction>) {
        val recentList = ArrayList<Any>()
        for (element in topupBillsTrackRecent) {
            recentList.add(DataLayer.mapOf(
                    DigitalTopupEventTracking.EnhanceEccomerce.NAME, element.itemRecent.clientNumber,
                    DigitalTopupEventTracking.EnhanceEccomerce.ID, "none",
                    DigitalTopupEventTracking.EnhanceEccomerce.PRICE, "0",
                    DigitalTopupEventTracking.EnhanceEccomerce.BRAND, "none",
                    DigitalTopupEventTracking.EnhanceEccomerce.CATEGORY, getTrackingCategoryName(element.categoryId),
                    DigitalTopupEventTracking.EnhanceEccomerce.LIST, "category ${getTrackingCategoryName(element.categoryId)} - " +
                    "product ${element.position} - " +
                    "${element.itemRecent.clientNumber} - ${element.itemRecent.title}",
                    DigitalTopupEventTracking.EnhanceEccomerce.POSITION, element.position))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", DigitalTopupEventTracking.Event.PRODUCT_VIEW,
                        "eventCategory", DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                        "eventAction", DigitalTopupEventTracking.Action.RECENT_ICON_IMPRESSION,
                        "eventLabel", "none",
                        "ecommerce",
                        DataLayer.mapOf(
                                "impressions",
                                DataLayer.listOf(
                                        *recentList.toTypedArray()
                                )
                        )
                )
        )

    }

    fun clickEnhanceCommerceRecentTransaction(itemRecent: TopupBillsRecommendation,
                                              operatorName: String, position: Int) {
        val recentList = ArrayList<Any>()
        recentList.add(DataLayer.mapOf(
                DigitalTopupEventTracking.EnhanceEccomerce.NAME, itemRecent.clientNumber,
                DigitalTopupEventTracking.EnhanceEccomerce.ID, "none",
                DigitalTopupEventTracking.EnhanceEccomerce.PRICE, "0",
                DigitalTopupEventTracking.EnhanceEccomerce.BRAND, operatorName,
                DigitalTopupEventTracking.EnhanceEccomerce.CATEGORY, getTrackingCategoryName(itemRecent.categoryId),
                DigitalTopupEventTracking.EnhanceEccomerce.POSITION, position))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", DigitalTopupEventTracking.Event.PRODUCT_CLICK,
                        "eventCategory", DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                        "eventAction", DigitalTopupEventTracking.Action.CLICK_RECENT_ICON,
                        "eventLabel", "${getTrackingCategoryName(itemRecent.categoryId)} - $position",
                        "ecommerce",
                        DataLayer.mapOf("click",
                                DataLayer.mapOf(
                                        "actionField",
                                        DataLayer.mapOf("list",
                                                "category id ${itemRecent.categoryId} - " +
                                                        "product $position - " +
                                                        "${itemRecent.clientNumber} - ${itemRecent.title}"
                                        ),
                                        "products", recentList.toArray()
                                )
                        )
                )
        )
    }

    fun impressionEnhanceCommercePromoList(topupBillsTrackPromoList: List<TopupBillsTrackPromo>) {
        val promoList = ArrayList<Any>()
        for (promo in topupBillsTrackPromoList) {
            promoList.add(DataLayer.mapOf(
                    DigitalTopupEventTracking.EnhanceEccomerce.ID, promo.promoItem.id,
                    DigitalTopupEventTracking.EnhanceEccomerce.NAME, "/deals-popular suggestion",
                    DigitalTopupEventTracking.EnhanceEccomerce.POSITION, promo.position,
                    DigitalTopupEventTracking.EnhanceEccomerce.CREATIVE, promo.promoItem.title,
                    DigitalTopupEventTracking.EnhanceEccomerce.CATEGORY, "none",
                    DigitalTopupEventTracking.EnhanceEccomerce.PROMO_ID, promo.promoItem.id,
                    DigitalTopupEventTracking.EnhanceEccomerce.PROMO_CODE, promo.promoItem.promoCode))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", DigitalTopupEventTracking.Event.PROMO_VIEW,
                        "eventCategory", DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                        "eventAction", DigitalTopupEventTracking.Action.PROMO_DIGITAL_IMPRESSION,
                        "eventLabel", "none",
                        "ecommerce", DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                        "promotions", promoList.toArray()
                )
                )
                )
        )

    }

    fun clickEnhanceCommercePromo(topupBillsPromo: TopupBillsPromo, position: Int) {
        val promoList = ArrayList<Any>()
        promoList.add(DataLayer.mapOf(
                DigitalTopupEventTracking.EnhanceEccomerce.ID, topupBillsPromo.id,
                DigitalTopupEventTracking.EnhanceEccomerce.NAME, "/deals-popular suggestion",
                DigitalTopupEventTracking.EnhanceEccomerce.POSITION, position,
                DigitalTopupEventTracking.EnhanceEccomerce.CREATIVE, topupBillsPromo.title,
                DigitalTopupEventTracking.EnhanceEccomerce.CREATIVE_URL, "none",
                DigitalTopupEventTracking.EnhanceEccomerce.CATEGORY, "none",
                DigitalTopupEventTracking.EnhanceEccomerce.PROMO_ID, topupBillsPromo.id,
                DigitalTopupEventTracking.EnhanceEccomerce.PROMO_CODE, topupBillsPromo.promoCode))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", DigitalTopupEventTracking.Event.PROMO_CLICK,
                        "eventCategory", DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                        "eventAction", DigitalTopupEventTracking.Action.CLICK_PROMO_DIGITAL,
                        "eventLabel", "${topupBillsPromo.promoCode} - $position",
                        "ecommerce",
                        DataLayer.mapOf(
                                "promoClick",
                                DataLayer.mapOf(
                                        "promotions",
                                        promoList.toArray()
                                )
                        )
                )
        )
    }

    fun impressionFavoriteNumberChips(categoryId: Int, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalTopupEventTracking.Event.DIGITAL_GENERAL_EVENT_IRIS,
                TrackAppUtils.EVENT_CATEGORY, DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                TrackAppUtils.EVENT_ACTION, DigitalTopupEventTracking.Action.VIEW_FAVORITE_NUMBER_CHIP,
                TrackAppUtils.EVENT_LABEL, getTrackingCategoryName(categoryId),
                DigitalTopupEventTracking.Additional.BUSINESS_UNIT, DigitalTopupEventTracking.Additional.BUSINESS_UNIT_RECHARGE,
                DigitalTopupEventTracking.Additional.CURRENT_SITE, DigitalTopupEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE,
                DigitalTopupEventTracking.Additional.USER_ID, userId,
            )
        )
    }

    fun clickFavoriteNumberChips(categoryId: Int, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalTopupEventTracking.Event.DIGITAL_GENERAL_EVENT,
                TrackAppUtils.EVENT_CATEGORY, DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                TrackAppUtils.EVENT_ACTION, DigitalTopupEventTracking.Action.CLICK_FAVORITE_NUMBER_CHIP,
                TrackAppUtils.EVENT_LABEL, getTrackingCategoryName(categoryId),
                DigitalTopupEventTracking.Additional.BUSINESS_UNIT, DigitalTopupEventTracking.Additional.BUSINESS_UNIT_RECHARGE,
                DigitalTopupEventTracking.Additional.CURRENT_SITE, DigitalTopupEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE,
                DigitalTopupEventTracking.Additional.USER_ID, userId,
            )
        )
    }

    fun impressionFavoriteContactChips(categoryId: Int, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalTopupEventTracking.Event.DIGITAL_GENERAL_EVENT_IRIS,
                TrackAppUtils.EVENT_CATEGORY, DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                TrackAppUtils.EVENT_ACTION, DigitalTopupEventTracking.Action.VIEW_FAVORITE_CONTACT_CHIP,
                TrackAppUtils.EVENT_LABEL, getTrackingCategoryName(categoryId),
                DigitalTopupEventTracking.Additional.BUSINESS_UNIT, DigitalTopupEventTracking.Additional.BUSINESS_UNIT_RECHARGE,
                DigitalTopupEventTracking.Additional.CURRENT_SITE, DigitalTopupEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE,
                DigitalTopupEventTracking.Additional.USER_ID, userId,
            )
        )
    }

    fun clickFavoriteContactChips(categoryId: Int, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalTopupEventTracking.Event.DIGITAL_GENERAL_EVENT,
                TrackAppUtils.EVENT_CATEGORY, DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                TrackAppUtils.EVENT_ACTION, DigitalTopupEventTracking.Action.CLICK_FAVORITE_CONTACT_CHIP,
                TrackAppUtils.EVENT_LABEL, getTrackingCategoryName(categoryId),
                DigitalTopupEventTracking.Additional.BUSINESS_UNIT, DigitalTopupEventTracking.Additional.BUSINESS_UNIT_RECHARGE,
                DigitalTopupEventTracking.Additional.CURRENT_SITE, DigitalTopupEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE,
                DigitalTopupEventTracking.Additional.USER_ID, userId,
            )
        )
    }

    fun clickFavoriteNumberAutoComplete(categoryId: Int, operatorName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalTopupEventTracking.Event.DIGITAL_GENERAL_EVENT,
                TrackAppUtils.EVENT_CATEGORY, DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                TrackAppUtils.EVENT_ACTION, DigitalTopupEventTracking.Action.CLICK_AUTOCOMPLETE_FAVORITE_NUMBER,
                TrackAppUtils.EVENT_LABEL, "${getTrackingCategoryName(categoryId)} - $operatorName",
                DigitalTopupEventTracking.Additional.BUSINESS_UNIT, DigitalTopupEventTracking.Additional.BUSINESS_UNIT_RECHARGE,
                DigitalTopupEventTracking.Additional.CURRENT_SITE, DigitalTopupEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE,
                DigitalTopupEventTracking.Additional.USER_ID, userId,
            )
        )
    }

    fun clickFavoriteContactAutoComplete(categoryId: Int, operatorName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalTopupEventTracking.Event.DIGITAL_GENERAL_EVENT,
                TrackAppUtils.EVENT_CATEGORY, DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                TrackAppUtils.EVENT_ACTION, DigitalTopupEventTracking.Action.CLICK_AUTOCOMPLETE_FAVORITE_CONTACT,
                TrackAppUtils.EVENT_LABEL, "${getTrackingCategoryName(categoryId)} - $operatorName",
                DigitalTopupEventTracking.Additional.BUSINESS_UNIT, DigitalTopupEventTracking.Additional.BUSINESS_UNIT_RECHARGE,
                DigitalTopupEventTracking.Additional.CURRENT_SITE, DigitalTopupEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE,
                DigitalTopupEventTracking.Additional.USER_ID, userId,
            )
        )
     }

    private fun getTrackingCategoryName(categoryId: Int): String {
        return getCategoryName(categoryId).lowercase()
    }

    private fun getTrackingCategoryName(categoryId: String): String {
        return getCategoryName(categoryId.toIntSafely()).lowercase()
    }

    fun getCategoryName(categoryId: Int): String {
        return when (categoryId) {
            TelcoCategoryType.CATEGORY_PULSA -> TelcoComponentName.PRODUCT_PULSA
            TelcoCategoryType.CATEGORY_PAKET_DATA -> TelcoComponentName.PRODUCT_PAKET_DATA
            TelcoCategoryType.CATEGORY_ROAMING -> TelcoComponentName.PRODUCT_ROAMING
            GeneralCategoryType.CATEGORY_SIGNAL -> GeneralComponentName.CATEGORY_SIGNAL
            else -> TelcoComponentName.PRODUCT_PASCABAYAR
        }
    }
}
