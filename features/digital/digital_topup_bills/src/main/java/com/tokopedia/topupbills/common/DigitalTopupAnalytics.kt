package com.tokopedia.topupbills.common

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.topupbills.telco.data.TelcoProductDataCollection
import com.tokopedia.topupbills.telco.view.model.DigitalTrackProductTelco
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
}
