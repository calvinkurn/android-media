package com.tokopedia.vouchergame.common

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.common.topupbills.data.TopupBillsBanner
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackImpressionItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.vouchergame.common.VoucherGameEventTracking.*
import com.tokopedia.vouchergame.detail.data.VoucherGameProduct
import com.tokopedia.vouchergame.list.data.VoucherGameOperator


/**
 * @author by resakemal on 27/08/19.
 */

class VoucherGameAnalytics {

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

    fun impressionBanner(data: List<TopupBillsTrackImpressionItem<TopupBillsBanner>>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", Event.PROMO_VIEW,
                        "eventCategory", Category.DIGITAL_HOMEPAGE,
                        "eventAction", Action.BANNER_IMPRESSION,
                        "eventLabel", "",
                        "ecommerce", DataLayer.mapOf(
                        Event.PROMO_VIEW, DataLayer.mapOf(
                        EnhanceEccomerce.PROMOTIONS, createBannerEcommerceItem(data).toArray()
                )
                )
                )
        )
    }

    fun eventClickBanner(banner: TopupBillsTrackImpressionItem<TopupBillsBanner>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", Event.PROMO_CLICK,
                        "eventCategory", Category.DIGITAL_HOMEPAGE,
                        "eventAction", Action.CLICK_BANNER,
                        "eventLabel", "${banner.item.title} - ${banner.position}",
                        "ecommerce", DataLayer.mapOf(
                        Event.PROMO_VIEW, DataLayer.mapOf(
                        EnhanceEccomerce.PROMOTIONS, createBannerEcommerceItem(listOf(banner)).toArray()
                )
                )
                )
        )
    }

    private fun createBannerEcommerceItem(data: List<TopupBillsTrackImpressionItem<TopupBillsBanner>>): ArrayList<Any> {
        val bannerList = ArrayList<Any>()
        for (banner in data) {
            banner.item.run {
                val mappedData = DataLayer.mapOf(
                        EnhanceEccomerce.ID, id,
                        EnhanceEccomerce.NAME, "promo slot name",
                        EnhanceEccomerce.CREATIVE, imageFilename,
                        EnhanceEccomerce.CREATIVE_URL, imageUrl,
                        EnhanceEccomerce.POSITION, banner.position
//                        ,EnhanceEccomerce.CATEGORY, it.product.attributes.price
                )
                if (promoCode.isNotEmpty()) mappedData[EnhanceEccomerce.PROMO_CODE] = promoCode
                bannerList.add(mappedData)
            }
        }
        return bannerList
    }

    fun impressionOperatorCardSearchResult(query: String, data: List<TopupBillsTrackImpressionItem<VoucherGameOperator>>) {
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

    fun eventClickOperatorCardSearchResult(operatorName: String,
                                           position: Int,
                                           operator: TopupBillsTrackImpressionItem<VoucherGameOperator>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", Event.PROMO_CLICK,
                        "eventCategory", Category.DIGITAL_HOMEPAGE,
                        "eventAction", Action.CLICK_PRODUCT_CART_RESULT,
                        "eventLabel", "$operatorName - $position",
                        "ecommerce", DataLayer.mapOf(
                        Event.PROMO_VIEW, DataLayer.mapOf(
                        EnhanceEccomerce.PROMOTIONS, createOperatorEcommerceItem(listOf(operator)).toArray()
                )
                )
                )
        )
    }

    fun impressionOperatorCard(data: List<TopupBillsTrackImpressionItem<VoucherGameOperator>>) {
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

    fun eventClickOperatorCard(operatorName: String,
                               position: Int,
                               operator: TopupBillsTrackImpressionItem<VoucherGameOperator>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", Event.PROMO_CLICK,
                        "eventCategory", Category.DIGITAL_HOMEPAGE,
                        "eventAction", Action.CLICK_OPERATOR_CARD,
                        "eventLabel", "$operatorName - $position",
                        "ecommerce", DataLayer.mapOf(
                        Event.PROMO_VIEW, DataLayer.mapOf(
                        EnhanceEccomerce.PROMOTIONS, createOperatorEcommerceItem(listOf(operator)).toArray()
                )
                )
                )
        )
    }

    private fun createOperatorEcommerceItem(data: List<TopupBillsTrackImpressionItem<VoucherGameOperator>>): ArrayList<Any> {
        val operatorList = ArrayList<Any>()
        for (operator in data) {
            operator.item.run {
                operatorList.add(DataLayer.mapOf(
                        EnhanceEccomerce.ID, id,
                        EnhanceEccomerce.NAME, attributes.name,
                        EnhanceEccomerce.CREATIVE, attributes.name,
                        EnhanceEccomerce.POSITION, operator.position
//                        ,EnhanceEccomerce.CATEGORY, it.product.attributes.price
                ))
            }
        }
        return operatorList
    }

    fun impressionProductCard(data: List<TopupBillsTrackImpressionItem<VoucherGameProduct>>) {
        val productList = ArrayList<Any>()
        for (product in data) {
            product.item.run {
                productList.add(DataLayer.mapOf(
                        EnhanceEccomerce.NAME, attributes.detail,
                        EnhanceEccomerce.ID, id,
                        EnhanceEccomerce.PRICE, attributes.price,
//                        ,EnhanceEccomerce.CATEGORY, it.product.attributes.price
                        EnhanceEccomerce.POSITION, product.position
                ))
            }
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", Event.PRODUCT_VIEW,
                        "eventCategory", Category.DIGITAL_HOMEPAGE,
                        "eventAction", Action.PRODUCT_CARD_IMPRESSION,
                        "eventLabel", "",
                        "ecommerce", DataLayer.mapOf(
                        "currency", "IDR",
                        "impressions", productList.toArray()
                )
                )
        )
    }

    fun eventClickProductCard(operatorName: String,
                         productName: String,
                         position: Int,
                         product: TopupBillsTrackImpressionItem<VoucherGameProduct>) {
        val productList = ArrayList<Any>()
        product.item.run {
            productList.add(DataLayer.mapOf(
                    EnhanceEccomerce.NAME, attributes.detail,
                    EnhanceEccomerce.ID, id,
                    EnhanceEccomerce.PRICE, attributes.price,
//                        ,EnhanceEccomerce.CATEGORY, it.product.attributes.price
                    "list", operatorName,
                    EnhanceEccomerce.POSITION, product.position
            ))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", Event.PRODUCT_CLICK,
                        "eventCategory", Category.DIGITAL_HOMEPAGE,
                        "eventAction", Action.CLICK_PRODUCT_CARD,
                        "eventLabel", "$productName - $position",
                        "ecommerce", DataLayer.mapOf(
                        "click", DataLayer.mapOf(
                        "actionField", DataLayer.mapOf("list", operatorName),
                        "products", productList.toArray()
                )
                )
                )
        )
    }

    fun eventClickBuy(categoryName: String,
                      operatorName: String,
                      instantCheckout: Boolean = false,
                      product: TopupBillsTrackImpressionItem<VoucherGameProduct>) {
        val instantCheckoutLabel = if (instantCheckout) "instant" else "no instant"
        val productList = ArrayList<Any>()
        product.item.run {
            productList.add(DataLayer.mapOf(
                    EnhanceEccomerce.NAME, attributes.detail,
                    EnhanceEccomerce.ID, id,
                    EnhanceEccomerce.PRICE, attributes.price,
//                        EnhanceEccomerce.CATEGORY, categoryName,
                    "list", operatorName,
                    "quantity"
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
