package com.tokopedia.vouchergame.common

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.common.topupbills.data.TopupBillsBanner
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.vouchergame.common.VoucherGameEventTracking.*
import com.tokopedia.vouchergame.detail.data.VoucherGameProduct
import com.tokopedia.vouchergame.list.data.VoucherGameOperator


/**
 * @author by resakemal on 27/08/19.
 */

class VoucherGameAnalytics {

    var categoryName = ""

    fun eventClickViewAllBanner() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_ALL_BANNER,
                ""
        ))
    }

    fun eventClickSearchBox() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_SEARCH_BOX,
                ""
        ))
    }

    fun eventClickSearchResult(query: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_SEARCH_RESULT,
                query
        ))
    }

    fun eventClearSearchBox() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CLEAR_SEARCH_BOX,
                ""
        ))
    }

    fun eventInputNumber() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_CATEGORY,
                Category.DIGITAL_CATEGORY,
                Action.INPUT_NUMBER,
                ""
        ))
    }

    fun eventClickBackButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_BACK_BUTTON,
                ""
        ))
    }

    fun eventClickInfoButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_HOMEPAGE,
                Action.CLICK_INFO_BUTTON,
                ""
        ))
    }

    fun impressionBanner(banner: TopupBillsBanner, position: Int) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", Event.PROMO_VIEW,
                        "eventCategory", Category.DIGITAL_HOMEPAGE,
                        "eventAction", Action.BANNER_IMPRESSION,
                        "eventLabel", "${banner.title} - $position",
                        "ecommerce", DataLayer.mapOf(
                        Event.PROMO_VIEW, DataLayer.mapOf(
                        EnhanceEccomerce.PROMOTIONS, createBannerEcommerceItem(listOf(banner)).toArray()
                )
                )
                )
        )
    }

    fun eventClickBanner(banner: TopupBillsBanner, position: Int) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", Event.PROMO_CLICK,
                        "eventCategory", Category.DIGITAL_HOMEPAGE,
                        "eventAction", Action.CLICK_BANNER,
                        "eventLabel", "${banner.title} - $position",
                        "ecommerce", DataLayer.mapOf(
                        Event.PROMO_CLICK, DataLayer.mapOf(
                        EnhanceEccomerce.PROMOTIONS, createBannerEcommerceItem(listOf(banner)).toArray()
                )
                )
                )
        )
    }

    private fun createBannerEcommerceItem(data: List<TopupBillsBanner>): ArrayList<Any> {
        val bannerList = ArrayList<Any>()
        for ((position, banner) in data.withIndex()) {
            with(banner) {
                val mappedData = DataLayer.mapOf(
                        EnhanceEccomerce.ID, id,
                        EnhanceEccomerce.NAME, "promo slot name",
                        EnhanceEccomerce.CREATIVE, title,
                        EnhanceEccomerce.CREATIVE_URL, linkUrl,
                        EnhanceEccomerce.POSITION, position
                )
                bannerList.add(mappedData)
            }
        }
        return bannerList
    }

    fun impressionOperatorCardSearchResult(query: String, data: List<VoucherGameOperator>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", Event.PROMO_VIEW,
                        "eventCategory", Category.DIGITAL_HOMEPAGE,
                        "eventAction", Action.PRODUCT_CART_RESULT_IMPRESSSION,
                        "eventLabel", query,
                        "ecommerce", DataLayer.mapOf(
                        Event.PROMO_VIEW, DataLayer.mapOf(
                        EnhanceEccomerce.PROMOTIONS, createOperatorEcommerceItem(data).toArray()
                )
                )
                )
        )
    }

    fun eventClickOperatorCardSearchResult(operator: VoucherGameOperator) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", Event.PROMO_CLICK,
                        "eventCategory", Category.DIGITAL_HOMEPAGE,
                        "eventAction", Action.CLICK_PRODUCT_CART_RESULT,
                        "eventLabel", "${operator.attributes.name} - ${operator.position}",
                        "ecommerce", DataLayer.mapOf(
                        Event.PROMO_CLICK, DataLayer.mapOf(
                        EnhanceEccomerce.PROMOTIONS, createOperatorEcommerceItem(listOf(operator)).toArray()
                )
                )
                )
        )
    }

    fun impressionOperatorCard(data: List<VoucherGameOperator>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", Event.PROMO_VIEW,
                        "eventCategory", Category.DIGITAL_HOMEPAGE,
                        "eventAction", Action.OPERATOR_CARD_IMPRESSION,
                        "eventLabel", "",
                        "ecommerce", DataLayer.mapOf(
                        Event.PROMO_VIEW, DataLayer.mapOf(
                        EnhanceEccomerce.PROMOTIONS, createOperatorEcommerceItem(data).toArray()
                )
                )
                )
        )
    }

    fun eventClickOperatorCard(operator: VoucherGameOperator) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", Event.PROMO_CLICK,
                        "eventCategory", Category.DIGITAL_HOMEPAGE,
                        "eventAction", Action.CLICK_OPERATOR_CARD,
                        "eventLabel", "${operator.attributes.name} - ${operator.position}",
                        "ecommerce", DataLayer.mapOf(
                        Event.PROMO_CLICK, DataLayer.mapOf(
                        EnhanceEccomerce.PROMOTIONS, createOperatorEcommerceItem(listOf(operator)).toArray()
                )
                )
                )
        )
    }

    private fun createOperatorEcommerceItem(data: List<VoucherGameOperator>): ArrayList<Any> {
        val operatorList = ArrayList<Any>()
        for (operator in data) {
            with(operator) {
                val mappedData = DataLayer.mapOf(
                        EnhanceEccomerce.ID, id,
                        EnhanceEccomerce.NAME, attributes.name,
                        EnhanceEccomerce.CREATIVE, attributes.name,
                        EnhanceEccomerce.POSITION, position
                )
                if (categoryName.isNotEmpty()) mappedData[EnhanceEccomerce.CATEGORY] = categoryName
                operatorList.add(mappedData)
            }
        }
        return operatorList
    }

    fun impressionProductCard(products: List<VoucherGameProduct>, operatorName: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", Event.PRODUCT_VIEW,
                        "eventCategory", Category.DIGITAL_HOMEPAGE,
                        "eventAction", Action.PRODUCT_CARD_IMPRESSION,
                        "eventLabel", "",
                        "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", createProductEcommerceItem(products, operatorName).toArray()
                )
                )
        )
    }

    fun eventClickProductCard(product: VoucherGameProduct, operatorName: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", Event.PRODUCT_CLICK,
                        "eventCategory", Category.DIGITAL_HOMEPAGE,
                        "eventAction", Action.CLICK_PRODUCT_CARD,
                        "eventLabel", "${product.attributes.desc} - ${product.position}",
                        "ecommerce", DataLayer.mapOf(
                        "click", DataLayer.mapOf(
                        "actionField", DataLayer.mapOf("list", operatorName),
                        "products", createProductEcommerceItem(listOf(product), operatorName).toArray()
                )
                )
                )
        )
    }

    private fun createProductEcommerceItem(data: List<VoucherGameProduct>, operatorName: String): ArrayList<Any> {
        val productList = ArrayList<Any>()
        for (product in data) {
            with(product) {
                val mappedData = DataLayer.mapOf(
                        EnhanceEccomerce.NAME, attributes.desc,
                        EnhanceEccomerce.ID, id,
                        EnhanceEccomerce.PRICE, attributes.pricePlain,
                        "list", operatorName,
                        EnhanceEccomerce.POSITION, position
                )
                if (categoryName.isNotEmpty()) mappedData[EnhanceEccomerce.CATEGORY] = categoryName
                productList.add(mappedData)
            }
        }
        return productList
    }

    fun eventClickBuy(categoryName: String,
                      operatorName: String,
                      instantCheckout: Boolean = false,
                      product: VoucherGameProduct) {
        val instantCheckoutLabel = if (instantCheckout) "instant" else "no instant"
        val productList = ArrayList<Any>()
        with(product) {
            productList.add(DataLayer.mapOf(
                    EnhanceEccomerce.NAME, attributes.desc,
                    EnhanceEccomerce.ID, id,
                    EnhanceEccomerce.PRICE, attributes.price,
                    EnhanceEccomerce.CATEGORY, categoryName,
                    "list", operatorName,
                    "quantity", 1
            ))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", Event.ADD_TO_CART,
                        "eventCategory", Category.DIGITAL_NATIVE,
                        "eventAction", Action.CLICK_BUY,
                        "eventLabel", "$categoryName - $operatorName - $instantCheckoutLabel",
                        "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "add", DataLayer.mapOf(
                        "products", productList.toArray()
                )
                )
                )
        )
    }
}
