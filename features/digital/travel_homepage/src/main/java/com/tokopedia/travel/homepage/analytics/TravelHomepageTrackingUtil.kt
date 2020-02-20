package com.tokopedia.travel.homepage.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingActionConstant.BANNER_CLICK
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingActionConstant.BANNER_CLICK_ALL
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingActionConstant.BANNER_IMPRESSION
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingActionConstant.DEALS_CLICK
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingActionConstant.DEALS_CLICK_ALL
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingActionConstant.DYNAMIC_ICON_CLICK
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingActionConstant.ORDER_CLICK
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingActionConstant.ORDER_CLICK_ALL
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingActionConstant.POPULAR_DESTINATION_CLICK
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingActionConstant.POPULAR_SEARCH_CLICK
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingActionConstant.RECENT_SEARCH_CLICK
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingCategoryConstant.TRAVEL_HOMEPAGE_CATEGORY
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingEEConstant.ACTION_FIELD
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingEEConstant.BRAND
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingEEConstant.CATEGORY
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingEEConstant.CREATIVE
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingEEConstant.CREATIVE_PREFIX
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingEEConstant.CREATIVE_URL
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingEEConstant.ECOMMERCE
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingEEConstant.ID
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingEEConstant.NAME
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingEEConstant.POSITION
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingEEConstant.PRICE
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingEEConstant.PRODUCTS
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingEEConstant.PROMOTIONS
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingEEConstant.VARIANT
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingEventNameConstant.CLICK_HOMEPAGE
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingEventNameConstant.PRODUCT_CLICK
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingEventNameConstant.PROMO_CLICK
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingEventNameConstant.PROMO_VIEW
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingLabelConstant.CLICK
import com.tokopedia.travel.homepage.data.TravelHomepageCategoryListModel
import com.tokopedia.travel.homepage.data.TravelHomepageDestinationModel
import com.tokopedia.travel.homepage.data.TravelHomepageSectionViewModel

/**
 * @author by furqan on 23/08/2019
 */
class TravelHomepageTrackingUtil {

    fun travelHomepageImpressionBanner(item: TravelCollectiveBannerModel.Banner, position: Int) {
        val products = mutableListOf<Any>()
        products.add(DataLayer.mapOf(
                NAME, "${item.attribute.promoCode} - slider banner",
                POSITION, position,
                ID, item.id.toInt(),
                CREATIVE, "$CREATIVE_PREFIX${item.attribute.promoCode}",
                CREATIVE_URL, item.attribute.imageUrl
        ))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_VIEW,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, BANNER_IMPRESSION,
                        TrackAppUtils.EVENT_LABEL, "$position - ${item.attribute.promoCode}",
                        ECOMMERCE, DataLayer.mapOf(PROMO_VIEW, DataLayer.mapOf(PROMOTIONS, products))
                ))
    }

    fun travelHomepageClickBanner(item: TravelCollectiveBannerModel.Banner, position: Int) {
        val products = mutableListOf<Any>()
        products.add(DataLayer.mapOf(
                NAME, "${item.attribute.promoCode} - slider banner",
                POSITION, position,
                ID, item.id.toInt(),
                CREATIVE, "$CREATIVE_PREFIX${item.attribute.promoCode}",
                CREATIVE_URL, item.attribute.imageUrl
        ))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, BANNER_CLICK,
                        TrackAppUtils.EVENT_LABEL, "$position - ${item.attribute.promoCode}",
                        ECOMMERCE, DataLayer.mapOf(PROMO_CLICK, DataLayer.mapOf(PROMOTIONS, products))
                ))
    }

    fun travelHomepageClickAllBanner() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, TRAVEL_HOMEPAGE_CATEGORY, BANNER_CLICK_ALL, CLICK)
    }

    fun travelHomepageClickCategory(item: TravelHomepageCategoryListModel.Category, position: Int) {
        val products = mutableListOf<Any>()
        products.add(DataLayer.mapOf(
                NAME, "${item.product} - dynamic icon",
                POSITION, position,
                ID, position,
                CREATIVE, "$CREATIVE_PREFIX${item.product}",
                CREATIVE_URL, item.attributes.imageUrl
        ))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, DYNAMIC_ICON_CLICK,
                        TrackAppUtils.EVENT_LABEL, "$position - ${item.product}",
                        ECOMMERCE, DataLayer.mapOf(PROMO_CLICK, DataLayer.mapOf(PROMOTIONS, products))
                ))
    }

    fun travelHomepageClickOrder(position: Int, categoryName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, TRAVEL_HOMEPAGE_CATEGORY, ORDER_CLICK, "$position - $categoryName")
    }

    fun travelHomepageClickAllOrder() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, TRAVEL_HOMEPAGE_CATEGORY, ORDER_CLICK_ALL, CLICK)
    }

    fun travelHomepageClickRecentSearch(position: Int, categoryName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, TRAVEL_HOMEPAGE_CATEGORY, RECENT_SEARCH_CLICK, "$position - $categoryName")
    }

    fun travelHomepageClickPopularSearch(position: Int, categoryName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, TRAVEL_HOMEPAGE_CATEGORY, POPULAR_SEARCH_CLICK, "$position - $categoryName")
    }

    fun travelHomepageClickDeal(item: TravelHomepageSectionViewModel.Item, position: Int) {
        val products = mutableListOf<Any>()
        products.add(DataLayer.mapOf(
                NAME, "${item.subtitle} - best deals widget",
                ID, item.product,
                PRICE, item.value,
                BRAND, item.title,
                CATEGORY, item.product,
                VARIANT, "",
                POSITION, position
        ))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, PRODUCT_CLICK,
                TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_CATEGORY,
                TrackAppUtils.EVENT_ACTION, DEALS_CLICK,
                TrackAppUtils.EVENT_LABEL, "$position - ${item.product}",
                ECOMMERCE, DataLayer.mapOf(CLICK, DataLayer.mapOf(
                ACTION_FIELD, DataLayer.mapOf("list", "/best deals"),
                PRODUCTS, products
        ))
        ))
    }

    fun travelHomepageClickAllDeals() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, TRAVEL_HOMEPAGE_CATEGORY, DEALS_CLICK_ALL, CLICK)
    }

    fun travelHomepageClickPopularDestination(item: TravelHomepageDestinationModel.Destination, position: Int) {
        val products = mutableListOf<Any>()
        products.add(DataLayer.mapOf(
                NAME, "${item.attributes.title} - dynamic banner",
                POSITION, position,
                ID, position,
                CREATIVE, "$CREATIVE_PREFIX${item.attributes.subtitle}",
                CREATIVE_URL, item.attributes.imageUrl
        ))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, POPULAR_DESTINATION_CLICK,
                        TrackAppUtils.EVENT_LABEL, "$position - ${item.attributes.title}",
                        ECOMMERCE, DataLayer.mapOf(PROMO_CLICK, DataLayer.mapOf(PROMOTIONS, products))
                ))
    }

}