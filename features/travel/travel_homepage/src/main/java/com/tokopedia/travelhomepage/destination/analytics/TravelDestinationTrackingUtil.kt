package com.tokopedia.travelhomepage.destination.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingActionConstant.BLOGSPOT_IMPRESSION
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingActionConstant.BOOKING_WIDGET_IMPRESSION
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingActionConstant.CLICK_BLOGSPOT
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingActionConstant.CLICK_BOOKING_WIDGET
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingActionConstant.CLICK_CROSS_SELL_WIDGET
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingActionConstant.CLICK_DEALS_WIDGET
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingActionConstant.CLICK_EVENTS_WIDGET
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingActionConstant.CLICK_VIEW_ALL_BLOGSPOT_WIDGET
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingActionConstant.CLICK_VIEW_ALL_DEALS_WIDGET
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingActionConstant.CLICK_VIEW_ALL_EVENTS_WIDGET
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingActionConstant.CROSS_SELL_WIDGET_IMPRESSION
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingActionConstant.DEALS_WIDGET_IMPRESSION
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingActionConstant.EVENTS_WIDGET_IMPRESSION
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingCategoryConstant.TRAVEL_HOMEPAGE_DESTINATION_CATEGORY
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingEventNameConstant.PRODUCT_CLICK
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingEventNameConstant.PRODUCT_VIEW
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingEventNameConstant.PROMO_CLICK
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingEventNameConstant.PROMO_VIEW
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingLabelConstant.VIEW_BLOGSPOT
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingLabelConstant.VIEW_BOOKING_WIDGET
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingLabelConstant.VIEW_CROSS_SELL_WIDGET
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingLabelConstant.VIEW_DEALS_WIDGET
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingLabelConstant.VIEW_EVENTS_WIDGET
import com.tokopedia.travelhomepage.destination.analytics.TravelHomepageTrackingEEConstant.ACTION_FIELD
import com.tokopedia.travelhomepage.destination.analytics.TravelHomepageTrackingEEConstant.BRAND
import com.tokopedia.travelhomepage.destination.analytics.TravelHomepageTrackingEEConstant.CATEGORY
import com.tokopedia.travelhomepage.destination.analytics.TravelHomepageTrackingEEConstant.CLICK
import com.tokopedia.travelhomepage.destination.analytics.TravelHomepageTrackingEEConstant.CREATIVE
import com.tokopedia.travelhomepage.destination.analytics.TravelHomepageTrackingEEConstant.CREATIVE_URL
import com.tokopedia.travelhomepage.destination.analytics.TravelHomepageTrackingEEConstant.CURRENCY_CODE
import com.tokopedia.travelhomepage.destination.analytics.TravelHomepageTrackingEEConstant.ECOMMERCE
import com.tokopedia.travelhomepage.destination.analytics.TravelHomepageTrackingEEConstant.ID
import com.tokopedia.travelhomepage.destination.analytics.TravelHomepageTrackingEEConstant.IDR
import com.tokopedia.travelhomepage.destination.analytics.TravelHomepageTrackingEEConstant.IMPRESSIONS
import com.tokopedia.travelhomepage.destination.analytics.TravelHomepageTrackingEEConstant.LIST
import com.tokopedia.travelhomepage.destination.analytics.TravelHomepageTrackingEEConstant.NAME
import com.tokopedia.travelhomepage.destination.analytics.TravelHomepageTrackingEEConstant.POSITION
import com.tokopedia.travelhomepage.destination.analytics.TravelHomepageTrackingEEConstant.PRICE
import com.tokopedia.travelhomepage.destination.analytics.TravelHomepageTrackingEEConstant.PRODUCTS
import com.tokopedia.travelhomepage.destination.analytics.TravelHomepageTrackingEEConstant.PROMOTIONS
import com.tokopedia.travelhomepage.destination.model.TravelArticleModel
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSectionModel
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEventNameConstant.CLICK_HOMEPAGE

/**
 * @author by jessica on 2020-01-13
 */

class TravelDestinationTrackingUtil {

    fun orderListImpression(list: List<TravelDestinationSectionModel.Item>, startingIndex: Int) {
        val products = mapToPromotionsProducts(list, startingIndex)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_VIEW,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_DESTINATION_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, BOOKING_WIDGET_IMPRESSION,
                        TrackAppUtils.EVENT_LABEL, VIEW_BOOKING_WIDGET,
                        ECOMMERCE, DataLayer.mapOf(PROMO_VIEW, DataLayer.mapOf(PROMOTIONS, products))
                ))
    }

    fun orderListClicked(item: TravelDestinationSectionModel.Item, itemPosition: Int) {
        val products = mapToPromotionsProducts(listOf(item), itemPosition)

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_DESTINATION_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, CLICK_BOOKING_WIDGET,
                        TrackAppUtils.EVENT_LABEL, "${itemPosition + 1} - ${item.product} - ${item.title}",
                        ECOMMERCE, DataLayer.mapOf(PROMO_CLICK, DataLayer.mapOf(PROMOTIONS, products))
                ))
    }

    fun cityRecommendationImpression(list: List<TravelDestinationSectionModel.Item>, startingIndex: Int) {
        val products = mapToPromotionsProducts(list, startingIndex)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_VIEW,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_DESTINATION_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, CROSS_SELL_WIDGET_IMPRESSION,
                        TrackAppUtils.EVENT_LABEL, VIEW_CROSS_SELL_WIDGET,
                        ECOMMERCE, DataLayer.mapOf(PROMO_VIEW, DataLayer.mapOf(PROMOTIONS, products))
                ))
    }

    fun cityRecommendationItemClicked(item: TravelDestinationSectionModel.Item, itemPosition: Int) {
        val products = mapToPromotionsProducts(listOf(item), itemPosition)

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_DESTINATION_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, CLICK_CROSS_SELL_WIDGET,
                        TrackAppUtils.EVENT_LABEL, "${itemPosition + 1} - ${item.product} - ${item.title}",
                        ECOMMERCE, DataLayer.mapOf(PROMO_CLICK, DataLayer.mapOf(PROMOTIONS, products))
                ))
    }

    fun cityEventsImpression(list: List<TravelDestinationSectionModel.Item>, startingIndex: Int) {
        val products = mapToImpressionsProduct(list, startingIndex, "/event")
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PRODUCT_VIEW,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_DESTINATION_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, EVENTS_WIDGET_IMPRESSION,
                        TrackAppUtils.EVENT_LABEL, VIEW_EVENTS_WIDGET,
                        ECOMMERCE, DataLayer.mapOf(CURRENCY_CODE, IDR,
                        IMPRESSIONS, products)
                ))
    }

    fun cityEventsClick(item: TravelDestinationSectionModel.Item, position: Int) {
        val products = mapToImpressionsProduct(listOf(item), position, "/event")
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PRODUCT_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_DESTINATION_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, CLICK_EVENTS_WIDGET,
                        TrackAppUtils.EVENT_LABEL, "${position + 1} - ${item.product} - ${item.title}",
                        ECOMMERCE, DataLayer.mapOf(CLICK, DataLayer.mapOf(ACTION_FIELD, DataLayer.mapOf(LIST, "/event",
                        PRODUCTS, products)))
                ))
    }

    fun cityEventsSeeAllClicked() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, TRAVEL_HOMEPAGE_DESTINATION_CATEGORY, CLICK_VIEW_ALL_EVENTS_WIDGET, CLICK)
    }

    fun cityDealsImpression(list: List<TravelDestinationSectionModel.Item>, startingIndex: Int) {
        val products = mapToImpressionsProduct(list, startingIndex, "/deals")
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PRODUCT_VIEW,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_DESTINATION_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, DEALS_WIDGET_IMPRESSION,
                        TrackAppUtils.EVENT_LABEL, VIEW_DEALS_WIDGET,
                        ECOMMERCE, DataLayer.mapOf(CURRENCY_CODE, IDR ,
                       IMPRESSIONS, products)
                ))
    }

    fun cityDealsClick(item: TravelDestinationSectionModel.Item, position: Int) {
        val products = mapToImpressionsProduct(listOf(item), position, "/deals")
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PRODUCT_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_DESTINATION_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, CLICK_DEALS_WIDGET,
                        TrackAppUtils.EVENT_LABEL, "${position + 1} - ${item.product} - ${item.title}",
                        ECOMMERCE, DataLayer.mapOf(CLICK, DataLayer.mapOf(ACTION_FIELD, DataLayer.mapOf(LIST, "/deals",
                        PRODUCTS, products)))
                ))
    }

    fun cityDealsSeeAllClicked() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, TRAVEL_HOMEPAGE_DESTINATION_CATEGORY, CLICK_VIEW_ALL_DEALS_WIDGET, CLICK)
    }


    fun cityArticleImpression(list: List<TravelArticleModel.Item>, startingIndex: Int) {
        val products = mapToArticleProduct(list, startingIndex)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_VIEW,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_DESTINATION_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, BLOGSPOT_IMPRESSION,
                        TrackAppUtils.EVENT_LABEL, VIEW_BLOGSPOT,
                        ECOMMERCE, DataLayer.mapOf(PROMO_VIEW, DataLayer.mapOf(PROMOTIONS, products))
                ))
    }

    fun cityArticleClicked(item: TravelArticleModel.Item, itemPosition: Int) {
        val products = mapToArticleProduct(listOf(item), itemPosition)

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_DESTINATION_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, CLICK_BLOGSPOT,
                        TrackAppUtils.EVENT_LABEL, "${itemPosition + 1} - ${item.title}",
                        ECOMMERCE, DataLayer.mapOf(PROMO_CLICK, DataLayer.mapOf(PROMOTIONS, products))
                ))
    }

    fun cityArticleClickSeeAll() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, TRAVEL_HOMEPAGE_DESTINATION_CATEGORY, CLICK_VIEW_ALL_BLOGSPOT_WIDGET, CLICK)
    }

    private fun mapToPromotionsProducts(list: List<TravelDestinationSectionModel.Item>, startingIndex: Int): MutableList<Any> {
        val products = mutableListOf<Any>()
        for ((index, item) in list.withIndex()) {
            val position = startingIndex + index + 1
            products.add(DataLayer.mapOf(
                    ID, position,
                    NAME, item.title,
                    CREATIVE, item.product,
                    CATEGORY, item.product,
                    POSITION, position
            ))
        }
        return products
    }

    private fun mapToImpressionsProduct(list: List<TravelDestinationSectionModel.Item>, startingIndex: Int, itemType: String): MutableList<Any> {
        val products = mutableListOf<Any>()
        for ((index, item) in list.withIndex()) {
            val position = startingIndex + index + 1
            products.add(DataLayer.mapOf(
                    ID, position,
                    NAME, item.title,
                    CATEGORY, item.product,
                    PRICE, getPriceValueFromString(item.value), // need to be number
                    BRAND, item.product,
                    LIST, itemType,
                    POSITION, position
            ))
        }
        return products
    }

    private fun mapToArticleProduct(list: List<TravelArticleModel.Item>, startingPosition: Int): MutableList<Any> {
        val products = mutableListOf<Any>()
        for ((index, item) in list.withIndex()) {
            val position = startingPosition + index + 1
            products.add(DataLayer.mapOf(
                    ID, position,
                    NAME, item.title,
                    CREATIVE, item.title,
                    CREATIVE_URL, item.webUrl,
                    CATEGORY, item.title,
                    POSITION, position
            ))
        }
        return products
    }

    private fun getPriceValueFromString(string: String): Int {
        return try {
            string.replace("Rp", "").replace(".", "").replace(" ", "").toInt()
        } catch (e: Exception) {
            0
        }
    }
}