package com.tokopedia.topupbills.common

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.topupbills.telco.data.TelcoProductDataCollection
import com.tokopedia.topupbills.telco.data.TelcoRecommendation
import com.tokopedia.topupbills.telco.view.model.DigitalTrackProductTelco
import com.tokopedia.topupbills.telco.view.model.DigitalTrackPromoTelco
import com.tokopedia.topupbills.telco.view.model.DigitalTrackRecentTransactionTelco
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
                "$categoryId - $operatorName"
        ))
    }

    fun eventInputNumberContactPicker(categoryId: Int, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.INPUT_FROM_CONTACT,
                "$categoryId - $operatorName"
        ))
    }

    fun eventClickOnContactPickerHomepage(categoryId: Int, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_ON_CONTACT,
                "$categoryId - $operatorName"
        ))
    }

    //not yet
    fun eventInputNumberWidget(categoryId: Int, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.INPUT_FROM_WIDGET,
                "$categoryId - $operatorName"
        ))
    }

    fun eventInputNumberFavorites(categoryId: Int, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.INPUT_FROM_FAVORITE_NUMBER,
                "$categoryId - $operatorName"
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
                categoryId.toString()
        ))
    }

    fun eventClickSeeMore(categoryId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_SEE_MORE,
                categoryId.toString()
        ))
    }

    fun eventCloseDetailProduct(categoryId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLOSE_DETAIL_PRODUCT,
                categoryId.toString()
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

    fun eventClickTelcoTab(headerTab: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_TELCO_TAB,
                "$headerTab"
        ))
    }

    fun impressionEnhanceCommerceProduct(digitalTrackProductTelcoList: List<DigitalTrackProductTelco>, categoryName: String) {
        val productTelcoList = ArrayList<Any>()
        for (i in 0 until digitalTrackProductTelcoList.size) {
            val product = digitalTrackProductTelcoList[i]
            productTelcoList.add(DataLayer.mapOf(
                    DigitalTopupEventTracking.EnhanceEccomerce.NAME, product.itemProduct.product.attributes.desc,
                    DigitalTopupEventTracking.EnhanceEccomerce.ID, product.itemProduct.product.id,
                    DigitalTopupEventTracking.EnhanceEccomerce.PRICE, product.itemProduct.product.attributes.price,
                    DigitalTopupEventTracking.EnhanceEccomerce.BRAND, "none",
                    DigitalTopupEventTracking.EnhanceEccomerce.CATEGORY, categoryName,
                    DigitalTopupEventTracking.EnhanceEccomerce.LIST, "$categoryName - product ${product.position} - " +
                    "${product.itemProduct.product.attributes.desc}",
                    DigitalTopupEventTracking.EnhanceEccomerce.POSITION, product.position))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", DigitalTopupEventTracking.Event.PRODUCT_VIEW,
                        "eventCategory", DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                        "eventAction", DigitalTopupEventTracking.Action.PRODUCT_CARD_IMPRESSION,
                        "eventLabel", categoryName,
                        "ecommerce", DataLayer.mapOf(
                        "impressions", DataLayer.mapOf(
                        "products", productTelcoList.toArray()
                )
                )
                )
        )

    }

    fun clickEnhanceCommerceProduct(itemProduct: TelcoProductDataCollection, position: Int, categoryName: String) {
        val trackProduct = DataLayer.mapOf(
                DigitalTopupEventTracking.EnhanceEccomerce.NAME, itemProduct.product.attributes.desc,
                DigitalTopupEventTracking.EnhanceEccomerce.ID, itemProduct.product.id,
                DigitalTopupEventTracking.EnhanceEccomerce.PRICE, itemProduct.product.attributes.price,
                DigitalTopupEventTracking.EnhanceEccomerce.BRAND, "none",
                DigitalTopupEventTracking.EnhanceEccomerce.CATEGORY, categoryName,
                DigitalTopupEventTracking.EnhanceEccomerce.POSITION, position)

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", DigitalTopupEventTracking.Event.PRODUCT_CLICK,
                        "eventCategory", DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                        "eventAction", DigitalTopupEventTracking.Action.CLICK_PRODUCT_CARD,
                        "eventLabel", categoryName,
                        "ecommerce", DataLayer.mapOf(
                        "click", DataLayer.mapOf(
                        "actionField", DataLayer.mapOf(
                        "list", "$categoryName - id product ${itemProduct.product.id} - " +
                        "position $position - ${itemProduct.product.attributes.desc}"
                ),
                        "products", trackProduct
                )
                )
                )
        )
    }

    fun impressionEnhanceCommerceRecentTransaction(digitalTrackRecentTelco: List<DigitalTrackRecentTransactionTelco>) {
        val recentList = ArrayList<Any>()
        for (i in 0 until digitalTrackRecentTelco.size) {
            val recentItem = digitalTrackRecentTelco[i]
            recentList.add(DataLayer.mapOf(
                    DigitalTopupEventTracking.EnhanceEccomerce.NAME, recentItem.itemRecent.clientNumber,
                    DigitalTopupEventTracking.EnhanceEccomerce.ID, "none",
                    DigitalTopupEventTracking.EnhanceEccomerce.PRICE, recentItem.itemRecent.title,
                    DigitalTopupEventTracking.EnhanceEccomerce.BRAND, "none",
                    DigitalTopupEventTracking.EnhanceEccomerce.CATEGORY, recentItem.categoryName,
                    DigitalTopupEventTracking.EnhanceEccomerce.LIST, "category ${recentItem.categoryName} - " +
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
                        "impressions", DataLayer.mapOf(
                        "products", recentList.toArray()
                )
                )
                )
        )

    }

    fun clickEnhanceCommerceRecentTransaction(itemRecent: TelcoRecommendation, categoryName: String, position: Int) {
        val trackRecent = DataLayer.mapOf(
                DigitalTopupEventTracking.EnhanceEccomerce.NAME, itemRecent.clientNumber,
                DigitalTopupEventTracking.EnhanceEccomerce.ID, "none",
                DigitalTopupEventTracking.EnhanceEccomerce.PRICE, itemRecent.title,
                DigitalTopupEventTracking.EnhanceEccomerce.BRAND, "none",
                DigitalTopupEventTracking.EnhanceEccomerce.CATEGORY, categoryName,
                DigitalTopupEventTracking.EnhanceEccomerce.POSITION, position)

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", DigitalTopupEventTracking.Event.PRODUCT_CLICK,
                        "eventCategory", DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                        "eventAction", DigitalTopupEventTracking.Action.CLICK_RECENT_ICON,
                        "eventLabel", "$categoryName - $position",
                        "ecommerce", DataLayer.mapOf(
                        "click", DataLayer.mapOf(
                        "actionField", DataLayer.mapOf(
                        "list", "category id ${itemRecent.categoryId} - " +
                        "product $position - " +
                        "${itemRecent.clientNumber} - ${itemRecent.title}"
                ),
                        "products", trackRecent
                )
                )
                )
        )
    }

    fun impressionEnhanceCommercePromoList(digitalTrackPromoList: List<DigitalTrackPromoTelco>) {
        val promoList = ArrayList<Any>()
        for (i in 0 until digitalTrackPromoList.size) {
            val promo = digitalTrackPromoList[i]
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
}
