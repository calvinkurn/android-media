package com.tokopedia.topupbills.common.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.common.topupbills.data.TopupBillsPromo
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackPromo
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackRecentTransaction
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.data.constant.TelcoCategoryType
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentName
import com.tokopedia.topupbills.telco.view.model.DigitalTrackProductTelco
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils


/**
 * @author by resakemal on 05/07/19.
 */

class DigitalTopupAnalytics {

    fun eventOpenScreen(userId: String, categoryId: Int) {
        val categoryName = getCategoryName(categoryId)
        val stringScreenName = StringBuilder(DigitalTopupEventTracking.Additional.DIGITAL_SCREEN_NAME)
        stringScreenName.append(categoryName.toLowerCase())

        val mapOpenScreen = HashMap<String, String>()
        mapOpenScreen[DigitalTopupEventTracking.Additional.IS_LOGIN_STATUS] = if (userId.isEmpty())  "false" else "true"
        mapOpenScreen[DigitalTopupEventTracking.Additional.BUSINESS_UNIT] = DigitalTopupEventTracking.Additional.BUSINESS_UNIT_RECHARGE
        mapOpenScreen[DigitalTopupEventTracking.Additional.CURRENT_SITE] = DigitalTopupEventTracking.Additional.CURRENT_SITE_RECHARGE
        mapOpenScreen[DigitalTopupEventTracking.Additional.USER_ID] = userId
        mapOpenScreen[DigitalTopupEventTracking.Additional.CATEGORY] = categoryName
        mapOpenScreen[DigitalTopupEventTracking.Additional.CATEGORY_ID] = categoryId.toString()

        TrackApp.getInstance().gtm.sendScreenAuthenticated(stringScreenName.toString(), mapOpenScreen)
    }

    fun eventInputNumberManual(categoryId: Int, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.INPUT_MANUAL_NUMBER,
                "${getCategoryName(categoryId)} - $operatorName"
        ))
    }

    fun eventInputNumberContactPicker(categoryId: Int, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.INPUT_FROM_CONTACT,
                "${getCategoryName(categoryId)} - $operatorName"
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

    //not yet
    fun eventInputNumberWidget(categoryId: Int, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.INPUT_FROM_WIDGET,
                "${getCategoryName(categoryId)} - $operatorName"
        ))
    }

    fun eventInputNumberFavorites(categoryId: Int, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.INPUT_FROM_FAVORITE_NUMBER,
                "${getCategoryName(categoryId)} - $operatorName"
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
                getCategoryName(categoryId)
        ))
    }

    fun eventClickSeeMore(categoryId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_SEE_MORE,
                getCategoryName(categoryId)
        ))
    }

    fun eventCloseDetailProduct(categoryId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLOSE_DETAIL_PRODUCT,
                getCategoryName(categoryId)
        ))
    }

    fun eventClickCopyPromoCode(promoName: String, position: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.PROMO_CLICK,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.COPY_PROMO_DIGITAL,
                "$promoName - $position"
        ))
    }

    fun eventClickTelcoPrepaidCategory(categoryName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_TELCO_CATEGORY,
                "Prabayar - $categoryName"
        ))
    }

    fun eventClickCheckEnquiry(categoryId: Int, operatorName: String, userId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_CHECK_TAGIHAN,
                "${getCategoryName(categoryId)} - $operatorName")
        sendGeneralEvent(mapEvent, userId)
    }

    fun eventClickTabMenuTelco(categoryId: Int, userId: String, action: String) {
        val mapEvent = TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                action,
                "${getCategoryName(categoryId)}")
        sendGeneralEvent(mapEvent, userId)
    }

    fun eventClickDotsMenuTelco(categoryId: String, userId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_DOTS_MENU,
                "${getCategoryName(categoryId.toInt())}")
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

    fun impressionEnhanceCommerceProduct(digitalTrackProductTelcoList: List<DigitalTrackProductTelco>, operatorName: String,
                                         userId: String) {
        val productTelcoList = ArrayList<Any>()
        for (element in digitalTrackProductTelcoList) {
            productTelcoList.add(DataLayer.mapOf(
                    DigitalTopupEventTracking.EnhanceEccomerce.NAME, element.itemProduct.attributes.desc,
                    DigitalTopupEventTracking.EnhanceEccomerce.ID, element.itemProduct.id,
                    DigitalTopupEventTracking.EnhanceEccomerce.PRICE, element.itemProduct.attributes.pricePlain.toString(),
                    DigitalTopupEventTracking.EnhanceEccomerce.BRAND, "none",
                    DigitalTopupEventTracking.EnhanceEccomerce.CATEGORY, getCategoryName(element.itemProduct.attributes.categoryId),
                    DigitalTopupEventTracking.EnhanceEccomerce.LIST, "${getCategoryName(element.itemProduct.attributes.categoryId)} - product ${element.position} - " +
                    "${element.itemProduct.attributes.desc}",
                    DigitalTopupEventTracking.EnhanceEccomerce.POSITION, element.position))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", DigitalTopupEventTracking.Event.PRODUCT_VIEW,
                        "eventCategory", DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                        "eventAction", DigitalTopupEventTracking.Action.PRODUCT_CARD_IMPRESSION,
                        "eventLabel", "${getCategoryName(digitalTrackProductTelcoList[0].itemProduct.attributes.categoryId)} - $operatorName",
                        DigitalTopupEventTracking.Additional.CURRENT_SITE, DigitalTopupEventTracking.Additional.CURRENT_SITE_RECHARGE,
                        DigitalTopupEventTracking.Additional.USER_ID, userId,
                        DigitalTopupEventTracking.Additional.BUSINESS_UNIT, DigitalTopupEventTracking.Additional.BUSINESS_UNIT_RECHARGE,
                        "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(
                        *productTelcoList.toTypedArray()
                )
                )
                )
        )

    }

    fun clickEnhanceCommerceProduct(itemProduct: TelcoProduct, position: Int,
                                    operatorName: String, userId: String, itemList: String) {
        val productTelcoList = ArrayList<Any>()
        productTelcoList.add(DataLayer.mapOf(
                DigitalTopupEventTracking.EnhanceEccomerce.NAME, itemProduct.attributes.desc,
                DigitalTopupEventTracking.EnhanceEccomerce.ID, itemProduct.id,
                DigitalTopupEventTracking.EnhanceEccomerce.PRICE, itemProduct.attributes.pricePlain.toString(),
                DigitalTopupEventTracking.EnhanceEccomerce.BRAND, operatorName,
                DigitalTopupEventTracking.EnhanceEccomerce.CATEGORY, getCategoryName(itemProduct.attributes.categoryId),
                DigitalTopupEventTracking.EnhanceEccomerce.LIST, itemProduct.attributes.desc,
                DigitalTopupEventTracking.EnhanceEccomerce.POSITION, position))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", DigitalTopupEventTracking.Event.PRODUCT_CLICK,
                        "eventCategory", DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                        "eventAction", DigitalTopupEventTracking.Action.CLICK_PRODUCT_CARD,
                        "eventLabel", "${getCategoryName(itemProduct.attributes.categoryId)} - $operatorName - ${itemProduct.attributes.desc}",
                        "item_list", itemList,
                        DigitalTopupEventTracking.Additional.CURRENT_SITE, DigitalTopupEventTracking.Additional.CURRENT_SITE_RECHARGE,
                        DigitalTopupEventTracking.Additional.USER_ID, userId,
                        DigitalTopupEventTracking.Additional.BUSINESS_UNIT, DigitalTopupEventTracking.Additional.BUSINESS_UNIT_RECHARGE,
                        "ecommerce", DataLayer.mapOf(
                        "click", DataLayer.mapOf(
                        "actionField", DataLayer.mapOf(
                        "list", "${itemProduct.attributes.desc}"),
                        "products", productTelcoList.toArray()
                )
                )
                )
        )
    }

    fun impressionPickProductDetail(itemProduct: TelcoProduct,
                                    operatorName: String, userId: String) {
        val productTelcoList = ArrayList<Any>()
        productTelcoList.add(DataLayer.mapOf(
                DigitalTopupEventTracking.EnhanceEccomerce.NAME, itemProduct.attributes.desc,
                DigitalTopupEventTracking.EnhanceEccomerce.ID, itemProduct.id,
                DigitalTopupEventTracking.EnhanceEccomerce.PRICE, itemProduct.attributes.pricePlain.toString(),
                DigitalTopupEventTracking.EnhanceEccomerce.BRAND, operatorName,
                DigitalTopupEventTracking.EnhanceEccomerce.CATEGORY, getCategoryName(itemProduct.attributes.categoryId),
                DigitalTopupEventTracking.EnhanceEccomerce.VARIANT, "none",
                DigitalTopupEventTracking.EnhanceEccomerce.QUANTITY, 1))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", DigitalTopupEventTracking.Event.ADD_TO_CART,
                        "eventCategory", DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                        "eventAction", DigitalTopupEventTracking.Action.CLICK_DETAIL_CLUSTER,
                        "eventLabel", "${getCategoryName(itemProduct.attributes.categoryId)} - $operatorName - ${itemProduct.attributes.desc}",
                        DigitalTopupEventTracking.Additional.CURRENT_SITE, DigitalTopupEventTracking.Additional.CURRENT_SITE_RECHARGE,
                        DigitalTopupEventTracking.Additional.USER_ID, userId,
                        DigitalTopupEventTracking.Additional.BUSINESS_UNIT, DigitalTopupEventTracking.Additional.BUSINESS_UNIT_RECHARGE,
                        "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "add", DataLayer.mapOf(
                        "products", productTelcoList.toArray()
                )
                )
                )
        )
    }

    fun impressionEnhanceCommerceRecentTransaction(topupBillsTrackRecent: List<TopupBillsTrackRecentTransaction>) {
        val recentList = ArrayList<Any>()
        for (i in 0 until topupBillsTrackRecent.size) {
            val recentItem = topupBillsTrackRecent[i]
            recentList.add(DataLayer.mapOf(
                    DigitalTopupEventTracking.EnhanceEccomerce.NAME, recentItem.itemRecent.clientNumber,
                    DigitalTopupEventTracking.EnhanceEccomerce.ID, "none",
                    DigitalTopupEventTracking.EnhanceEccomerce.PRICE, "0",
                    DigitalTopupEventTracking.EnhanceEccomerce.BRAND, "none",
                    DigitalTopupEventTracking.EnhanceEccomerce.CATEGORY, getCategoryName(recentItem.categoryId),
                    DigitalTopupEventTracking.EnhanceEccomerce.LIST, "category ${getCategoryName(recentItem.categoryId)} - " +
                    "product ${recentItem.position} - " +
                    "${recentItem.itemRecent.clientNumber} - ${recentItem.itemRecent.title}",
                    DigitalTopupEventTracking.EnhanceEccomerce.POSITION, recentItem.position))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", DigitalTopupEventTracking.Event.PRODUCT_VIEW,
                        "eventCategory", DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                        "eventAction", DigitalTopupEventTracking.Action.RECENT_ICON_IMPRESSION,
                        "eventLabel", "none",
                        "ecommerce", DataLayer.mapOf(
                        "impressions", DataLayer.listOf(
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
                DigitalTopupEventTracking.EnhanceEccomerce.CATEGORY, getCategoryName(itemRecent.categoryId),
                DigitalTopupEventTracking.EnhanceEccomerce.POSITION, position))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", DigitalTopupEventTracking.Event.PRODUCT_CLICK,
                        "eventCategory", DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                        "eventAction", DigitalTopupEventTracking.Action.CLICK_RECENT_ICON,
                        "eventLabel", "${getCategoryName(itemRecent.categoryId)} - $position",
                        "ecommerce", DataLayer.mapOf(
                        "click", DataLayer.mapOf(
                        "actionField", DataLayer.mapOf(
                        "list", "category id ${itemRecent.categoryId} - " +
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
        for (i in 0 until topupBillsTrackPromoList.size) {
            val promo = topupBillsTrackPromoList[i]
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
                        "ecommerce", DataLayer.mapOf(
                        "promoClick", DataLayer.mapOf(
                        "promotions", promoList.toArray()
                )
                )
                )
        )
    }

    private fun getCategoryName(categoryId: Int): String {
        return when (categoryId) {
            TelcoCategoryType.CATEGORY_PULSA -> TelcoComponentName.PRODUCT_PULSA.toLowerCase()
            TelcoCategoryType.CATEGORY_PAKET_DATA -> TelcoComponentName.PRODUCT_PAKET_DATA.toLowerCase()
            TelcoCategoryType.CATEGORY_ROAMING -> TelcoComponentName.PRODUCT_ROAMING.toLowerCase()
            else -> TelcoComponentName.PRODUCT_PASCABAYAR.toLowerCase()
        }
    }
}
