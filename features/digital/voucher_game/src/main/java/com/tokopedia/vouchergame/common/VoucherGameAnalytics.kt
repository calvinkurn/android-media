package com.tokopedia.vouchergame.common

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils


/**
 * @author by resakemal on 27/08/19.
 */

class VoucherGameAnalytics {

    fun eventClickViewAllBanner() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                VoucherGameEventTracking.Event.CLICK_HOMEPAGE,
                VoucherGameEventTracking.Category.DIGITAL_HOMEPAGE,
                VoucherGameEventTracking.Action.CLICK_ALL_BANNER,
                ""
        ))
    }

    fun eventClickSearchBox() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                VoucherGameEventTracking.Event.CLICK_HOMEPAGE,
                VoucherGameEventTracking.Category.DIGITAL_HOMEPAGE,
                VoucherGameEventTracking.Action.CLICK_SEARCH_BOX,
                ""
        ))
    }

    fun eventClickSearchResult() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                VoucherGameEventTracking.Event.CLICK_HOMEPAGE,
                VoucherGameEventTracking.Category.DIGITAL_HOMEPAGE,
                VoucherGameEventTracking.Action.CLICK_SEARCH_RESULT,
                ""
        ))
    }

    fun eventClearSearchBox() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                VoucherGameEventTracking.Event.CLICK_HOMEPAGE,
                VoucherGameEventTracking.Category.DIGITAL_HOMEPAGE,
                VoucherGameEventTracking.Action.CLEAR_SEARCH_BOX,
                ""
        ))
    }

    fun eventInputNumber() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                VoucherGameEventTracking.Event.CLICK_CATEGORY,
                VoucherGameEventTracking.Category.DIGITAL_CATEGORY,
                VoucherGameEventTracking.Action.INPUT_NUMBER,
                ""
        ))
    }

    fun eventClickBackButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                VoucherGameEventTracking.Event.CLICK_HOMEPAGE,
                VoucherGameEventTracking.Category.DIGITAL_HOMEPAGE,
                VoucherGameEventTracking.Action.CLICK_BACK_BUTTON,
                ""
        ))
    }

    fun eventClickInfoButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                VoucherGameEventTracking.Event.CLICK_HOMEPAGE,
                VoucherGameEventTracking.Category.DIGITAL_HOMEPAGE,
                VoucherGameEventTracking.Action.CLICK_INFO_BUTTON,
                ""
        ))
    }

    // TODO: Banner EnhanceEcommerce

//    fun impressionEnhanceCommerceProductCardResult(digitalTrackProductTelcoList: List<DigitalTrackProductTelco>) {
//        val productTelcoList = ArrayList<Any>()
//        for (i in 0 until digitalTrackProductTelcoList.size) {
//            val product = digitalTrackProductTelcoList[i]
//            productTelcoList.add(DataLayer.mapOf(
//                    VoucherGameEventTracking.EnhanceEccomerce.NAME, product.itemProduct.product.attributes.desc,
//                    VoucherGameEventTracking.EnhanceEccomerce.ID, product.itemProduct.product.id,
//                    VoucherGameEventTracking.EnhanceEccomerce.PRICE, product.itemProduct.product.attributes.price,
//                    VoucherGameEventTracking.EnhanceEccomerce.BRAND, "none",
//                    VoucherGameEventTracking.EnhanceEccomerce.CATEGORY, getCategoryName(product.itemProduct.product.attributes.categoryId),
//                    VoucherGameEventTracking.EnhanceEccomerce.LIST, "${getCategoryName(product.itemProduct.product.attributes.categoryId)} - product ${product.position} - " +
//                    "${product.itemProduct.product.attributes.desc}",
//                    VoucherGameEventTracking.EnhanceEccomerce.POSITION, product.position))
//        }
//
//        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
//                DataLayer.mapOf(
//                        "event", VoucherGameEventTracking.Event.PRODUCT_VIEW,
//                        "eventCategory", VoucherGameEventTracking.Category.DIGITAL_HOMEPAGE,
//                        "eventAction", VoucherGameEventTracking.Action.PRODUCT_CARD_IMPRESSION,
//                        "eventLabel", getCategoryName(digitalTrackProductTelcoList[0].itemProduct.product.attributes.categoryId),
//                        "ecommerce", DataLayer.mapOf(
//                        "impressions", DataLayer.mapOf(
//                        "products", productTelcoList.toArray()
//                )
//                )
//                )
//        )
//
//    }
//
//    fun clickEnhanceCommerceProduct(itemProduct: TelcoProductDataCollection, position: Int,
//                                    operatorName: String) {
//        val productTelcoList = ArrayList<Any>()
//        productTelcoList.add(DataLayer.mapOf(
//                VoucherGameEventTracking.EnhanceEccomerce.NAME, itemProduct.product.attributes.desc,
//                VoucherGameEventTracking.EnhanceEccomerce.ID, itemProduct.product.id,
//                VoucherGameEventTracking.EnhanceEccomerce.PRICE, itemProduct.product.attributes.price,
//                VoucherGameEventTracking.EnhanceEccomerce.BRAND, operatorName,
//                VoucherGameEventTracking.EnhanceEccomerce.CATEGORY, getCategoryName(itemProduct.product.attributes.categoryId),
//                VoucherGameEventTracking.EnhanceEccomerce.POSITION, position))
//
//        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
//                DataLayer.mapOf(
//                        "event", VoucherGameEventTracking.Event.PRODUCT_CLICK,
//                        "eventCategory", VoucherGameEventTracking.Category.DIGITAL_HOMEPAGE,
//                        "eventAction", VoucherGameEventTracking.Action.CLICK_PRODUCT_CARD,
//                        "eventLabel", getCategoryName(itemProduct.product.attributes.categoryId),
//                        "ecommerce", DataLayer.mapOf(
//                        "click", DataLayer.mapOf(
//                        "actionField", DataLayer.mapOf(
//                        "list", "${getCategoryName(itemProduct.product.attributes.categoryId)} - id product ${itemProduct.product.id} - " +
//                        "position $position - ${itemProduct.product.attributes.desc}"
//                ),
//                        "products", productTelcoList.toArray()
//                )
//                )
//                )
//        )
//    }
//
//    fun impressionEnhanceCommerceRecentTransaction(digitalTrackRecentTelco: List<DigitalTrackRecentTransactionTelco>) {
//        val recentList = ArrayList<Any>()
//        for (i in 0 until digitalTrackRecentTelco.size) {
//            val recentItem = digitalTrackRecentTelco[i]
//            recentList.add(DataLayer.mapOf(
//                    VoucherGameEventTracking.EnhanceEccomerce.NAME, recentItem.itemRecent.clientNumber,
//                    VoucherGameEventTracking.EnhanceEccomerce.ID, "none",
//                    VoucherGameEventTracking.EnhanceEccomerce.PRICE, "0",
//                    VoucherGameEventTracking.EnhanceEccomerce.BRAND, "none",
//                    VoucherGameEventTracking.EnhanceEccomerce.CATEGORY, getCategoryName(recentItem.categoryId),
//                    VoucherGameEventTracking.EnhanceEccomerce.LIST, "category ${getCategoryName(recentItem.categoryId)} - " +
//                    "product ${recentItem.position} - " +
//                    "${recentItem.itemRecent.clientNumber} - ${recentItem.itemRecent.title}",
//                    VoucherGameEventTracking.EnhanceEccomerce.POSITION, recentItem.position))
//        }
//
//        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
//                DataLayer.mapOf(
//                        "event", VoucherGameEventTracking.Event.PRODUCT_VIEW,
//                        "eventCategory", VoucherGameEventTracking.Category.DIGITAL_HOMEPAGE,
//                        "eventAction", VoucherGameEventTracking.Action.RECENT_ICON_IMPRESSION,
//                        "eventLabel", "none",
//                        "ecommerce", DataLayer.mapOf(
//                        "impressions", DataLayer.mapOf(
//                        "products", recentList.toArray()
//                )
//                )
//                )
//        )
//
//    }
//
//    fun clickEnhanceCommerceRecentTransaction(itemRecent: TelcoRecommendation,
//                                              operatorName: String, position: Int) {
//        val recentList = ArrayList<Any>()
//        recentList.add(DataLayer.mapOf(
//                VoucherGameEventTracking.EnhanceEccomerce.NAME, itemRecent.clientNumber,
//                VoucherGameEventTracking.EnhanceEccomerce.ID, "none",
//                VoucherGameEventTracking.EnhanceEccomerce.PRICE, "0",
//                VoucherGameEventTracking.EnhanceEccomerce.BRAND, operatorName,
//                VoucherGameEventTracking.EnhanceEccomerce.CATEGORY, getCategoryName(itemRecent.categoryId),
//                VoucherGameEventTracking.EnhanceEccomerce.POSITION, position))
//
//        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
//                DataLayer.mapOf(
//                        "event", VoucherGameEventTracking.Event.PRODUCT_CLICK,
//                        "eventCategory", VoucherGameEventTracking.Category.DIGITAL_HOMEPAGE,
//                        "eventAction", VoucherGameEventTracking.Action.CLICK_RECENT_ICON,
//                        "eventLabel", "${getCategoryName(itemRecent.categoryId)} - $position",
//                        "ecommerce", DataLayer.mapOf(
//                        "click", DataLayer.mapOf(
//                        "actionField", DataLayer.mapOf(
//                        "list", "category id ${itemRecent.categoryId} - " +
//                        "product $position - " +
//                        "${itemRecent.clientNumber} - ${itemRecent.title}"
//                ),
//                        "products", recentList.toArray()
//                )
//                )
//                )
//        )
//    }
//
//    fun impressionEnhanceCommercePromoList(digitalTrackPromoList: List<DigitalTrackPromoTelco>) {
//        val promoList = ArrayList<Any>()
//        for (i in 0 until digitalTrackPromoList.size) {
//            val promo = digitalTrackPromoList[i]
//            promoList.add(DataLayer.mapOf(
//                    VoucherGameEventTracking.EnhanceEccomerce.ID, promo.promoItem.id,
//                    VoucherGameEventTracking.EnhanceEccomerce.NAME, "/deals-popular suggestion",
//                    VoucherGameEventTracking.EnhanceEccomerce.POSITION, promo.position,
//                    VoucherGameEventTracking.EnhanceEccomerce.CREATIVE, promo.promoItem.title,
//                    VoucherGameEventTracking.EnhanceEccomerce.CATEGORY, "none",
//                    VoucherGameEventTracking.EnhanceEccomerce.PROMO_ID, promo.promoItem.id,
//                    VoucherGameEventTracking.EnhanceEccomerce.PROMO_CODE, promo.promoItem.promoCode))
//        }
//
//        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
//                DataLayer.mapOf(
//                        "event", VoucherGameEventTracking.Event.PROMO_VIEW,
//                        "eventCategory", VoucherGameEventTracking.Category.DIGITAL_HOMEPAGE,
//                        "eventAction", VoucherGameEventTracking.Action.PROMO_DIGITAL_IMPRESSION,
//                        "eventLabel", "none",
//                        "ecommerce", DataLayer.mapOf(
//                        "promoView", DataLayer.mapOf(
//                        "promotions", promoList.toArray()
//                )
//                )
//                )
//        )
//
//    }
//
//    fun clickEnhanceCommercePromo(telcoPromo: TelcoPromo, position: Int) {
//        val promoList = ArrayList<Any>()
//        promoList.add(DataLayer.mapOf(
//                VoucherGameEventTracking.EnhanceEccomerce.ID, telcoPromo.id,
//                VoucherGameEventTracking.EnhanceEccomerce.NAME, "/deals-popular suggestion",
//                VoucherGameEventTracking.EnhanceEccomerce.POSITION, position,
//                VoucherGameEventTracking.EnhanceEccomerce.CREATIVE, telcoPromo.title,
//                VoucherGameEventTracking.EnhanceEccomerce.CREATIVE_URL, "none",
//                VoucherGameEventTracking.EnhanceEccomerce.CATEGORY, "none",
//                VoucherGameEventTracking.EnhanceEccomerce.PROMO_ID, telcoPromo.id,
//                VoucherGameEventTracking.EnhanceEccomerce.PROMO_CODE, telcoPromo.promoCode))
//
//        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
//                DataLayer.mapOf(
//                        "event", VoucherGameEventTracking.Event.PROMO_CLICK,
//                        "eventCategory", VoucherGameEventTracking.Category.DIGITAL_HOMEPAGE,
//                        "eventAction", VoucherGameEventTracking.Action.CLICK_PROMO_DIGITAL,
//                        "eventLabel", telcoPromo.promoCode,
//                        "ecommerce", DataLayer.mapOf(
//                        "promoClick", DataLayer.mapOf(
//                        "promotions", promoList.toArray()
//                )
//                )
//                )
//        )
//    }
//
//    fun trackScreenNameTelco(screenName: String) {
//        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
//    }
//
//    private fun getCategoryName(categoryId: Int): String {
//        return when (categoryId) {
//            TelcoCategoryType.CATEGORY_PULSA -> TelcoComponentName.PRODUCT_PULSA.toLowerCase()
//            TelcoCategoryType.CATEGORY_PAKET_DATA -> TelcoComponentName.PRODUCT_PAKET_DATA.toLowerCase()
//            TelcoCategoryType.CATEGORY_ROAMING -> TelcoComponentName.PRODUCT_ROAMING.toLowerCase()
//            else -> TelcoComponentName.PRODUCT_PULSA.toLowerCase()
//        }
//    }
}
