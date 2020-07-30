package com.tokopedia.travelhomepage.homepage.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingActionConstant.BANNER_CLICK
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingActionConstant.BANNER_CLICK_ALL
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingActionConstant.BANNER_IMPRESSION
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingActionConstant.DYNAMIC_ICON_CLICK
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingActionConstant.LEGO_BANNER_CLICK
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingActionConstant.LEGO_BANNER_IMPRESSION
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingActionConstant.POPULAR_DESTINATION_CLICK
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingActionConstant.POPULAR_DESTINATION_IMPRESSION
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingActionConstant.PRODUCT_CARD_CLICK
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingActionConstant.PRODUCT_CARD_CLICK_SEE_ALL
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingActionConstant.PRODUCT_CARD_IMPRESSION
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingActionConstant.SQUARE_PRODUCT_CARD_CLICK
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingActionConstant.SQUARE_PRODUCT_CARD_CLICK_SEE_ALL
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingActionConstant.SQUARE_PRODUCT_CARD_IMPRESSION
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingCategoryConstant.BUSINESS_UNIT
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingCategoryConstant.CURRENT_SITE
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingCategoryConstant.SUBHOMEPAGE_CATEGORY_NAME
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingCategoryConstant.TOKOPEDIA_DIGITAL_SUBHOMEPAGE
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingCategoryConstant.TRAVEL_ENTERTAINMENT
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingCategoryConstant.TRAVEL_HOMEPAGE_CATEGORY
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEEConstant.ACTION_FIELD
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEEConstant.CATEGORY
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEEConstant.CREATIVE
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEEConstant.CREATIVE_PREFIX
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEEConstant.CREATIVE_URL
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEEConstant.CURRENCY_CODE
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEEConstant.ECOMMERCE
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEEConstant.ID
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEEConstant.IDR_DEFAULT_CURRENCY
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEEConstant.IMPRESSIONS
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEEConstant.LIST
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEEConstant.NAME
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEEConstant.POSITION
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEEConstant.PRICE
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEEConstant.PRODUCTS
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEEConstant.PROMOTIONS
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEventNameConstant.CLICK_EVENT
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEventNameConstant.CLICK_HOMEPAGE
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEventNameConstant.PRODUCT_CLICK
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEventNameConstant.PRODUCT_VIEW
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEventNameConstant.PROMO_CLICK
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingEventNameConstant.PROMO_VIEW
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingLabelConstant.CLICK
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageCategoryListModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageDestinationModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageSectionModel
import com.tokopedia.travelhomepage.homepage.data.widgetmodel.LegoBannerItemModel
import com.tokopedia.travelhomepage.homepage.data.widgetmodel.ProductGridCardItemModel

/**
 * @author by furqan on 23/08/2019
 */
class TravelHomepageTrackingUtil {

    fun travelHomepageImpressionBanner(item: TravelCollectiveBannerModel.Banner, position: Int) {

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_VIEW,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, BANNER_IMPRESSION,
                        TrackAppUtils.EVENT_LABEL, "${position + 1} - ${item.attribute.promoCode}",
                        ECOMMERCE, DataLayer.mapOf(PROMO_VIEW, DataLayer.mapOf(PROMOTIONS, mapToBannerList(listOf(item), position)))
                ))
    }

    fun travelHomepageClickBanner(item: TravelCollectiveBannerModel.Banner, position: Int) {

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, BANNER_CLICK,
                        TrackAppUtils.EVENT_LABEL, "${position + 1} - ${item.attribute.promoCode}",
                        ECOMMERCE, DataLayer.mapOf(PROMO_CLICK, DataLayer.mapOf(PROMOTIONS, mapToBannerList(listOf(item), position)))
                ))
    }

    fun travelHomepageClickAllBanner() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, TRAVEL_HOMEPAGE_CATEGORY, BANNER_CLICK_ALL, CLICK)
    }

    private fun mapToBannerList(list: List<TravelCollectiveBannerModel.Banner>, position: Int): List<Any> {

        val products = mutableListOf<Any>()
        for (item in list) {
            if (item.id.isEmpty()) item.id = "0"
            products.add(DataLayer.mapOf(
                    NAME, "${item.attribute.promoCode} - slider banner",
                    POSITION, position + 1,
                    ID, item.id.toInt(),
                    CREATIVE, "$CREATIVE_PREFIX${item.attribute.promoCode}",
                    CREATIVE_URL, item.attribute.imageUrl
            ))
        }
        return products
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

    fun travelHomepageDynamicBannerImpression(list: List<TravelHomepageDestinationModel.Destination>, componentPosition: Int, sectionTitle: String) {

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_VIEW,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, POPULAR_DESTINATION_IMPRESSION,
                        TrackAppUtils.EVENT_LABEL, "popular destination - $componentPosition",
                        CURRENT_SITE, TOKOPEDIA_DIGITAL_SUBHOMEPAGE,
                        BUSINESS_UNIT, TRAVEL_ENTERTAINMENT,
                        ECOMMERCE, DataLayer.mapOf(PROMO_VIEW, DataLayer.mapOf(PROMOTIONS, mapToPopularDestinationProduct(list, sectionTitle = sectionTitle)))
                ))
    }

    fun travelHomepageClickPopularDestination(item: TravelHomepageDestinationModel.Destination, position: Int, componentPosition: Int, sectionTitle: String) {

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, POPULAR_DESTINATION_CLICK,
                        TrackAppUtils.EVENT_LABEL, "popular destination - $componentPosition - $position - ${item.attributes.title}",
                        CURRENT_SITE, TOKOPEDIA_DIGITAL_SUBHOMEPAGE,
                        BUSINESS_UNIT, TRAVEL_ENTERTAINMENT,
                        ECOMMERCE, DataLayer.mapOf(PROMO_CLICK, DataLayer.mapOf(PROMOTIONS, mapToPopularDestinationProduct(listOf(item), position, sectionTitle)))
                ))
    }

    private fun mapToPopularDestinationProduct(list: List<TravelHomepageDestinationModel.Destination>,
                                               startingPosition: Int = 0, sectionTitle: String): List<Any> {
        val products = mutableListOf<Any>()
        for ((index, item) in list.withIndex()) {
            products.add(DataLayer.mapOf(
                    NAME, "popular destination widget - $sectionTitle",
                    POSITION, index + 1 + startingPosition,
                    ID, item.attributes.id,
                    CREATIVE, item.attributes.title,
                    CREATIVE_URL, item.attributes.imageUrl
            ))
        }
        return products
    }

    fun travelProductCardImpression(list: List<ProductGridCardItemModel>, componentPosition: Int, sectionTitle: String) {

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PRODUCT_VIEW,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, SQUARE_PRODUCT_CARD_IMPRESSION,
                        TrackAppUtils.EVENT_LABEL, "square product card - $componentPosition - $SUBHOMEPAGE_CATEGORY_NAME - $sectionTitle",
                        CURRENT_SITE, TOKOPEDIA_DIGITAL_SUBHOMEPAGE,
                        BUSINESS_UNIT, TRAVEL_ENTERTAINMENT,
                        ECOMMERCE, DataLayer.mapOf(CURRENCY_CODE, IDR_DEFAULT_CURRENCY,
                        IMPRESSIONS, mapToProductCardList(list))
                ))
    }

    fun travelProductCardClick(item: ProductGridCardItemModel, position: Int, componentPosition: Int, sectionTitle: String) {

        if (item.product.isEmpty()) item.product = SUBHOMEPAGE_CATEGORY_NAME

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PRODUCT_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, SQUARE_PRODUCT_CARD_CLICK,
                        TrackAppUtils.EVENT_LABEL, "square product card - $componentPosition - ${item.product} - ${position + 1} - ${item.id}",
                        CURRENT_SITE, TOKOPEDIA_DIGITAL_SUBHOMEPAGE,
                        BUSINESS_UNIT, TRAVEL_ENTERTAINMENT,
                        ECOMMERCE, DataLayer.mapOf(CLICK, DataLayer.mapOf(ACTION_FIELD, DataLayer.mapOf(LIST, "/${item.product}"),
                        PRODUCTS, mapToProductCardList(listOf(item), position)))
                ))
    }

    fun travelHomepageClickSeeAllProductCard(componentPosition: Int, sectionTitle: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, CLICK_EVENT,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, SQUARE_PRODUCT_CARD_CLICK_SEE_ALL,
                        TrackAppUtils.EVENT_LABEL, "square product card - $componentPosition - $SUBHOMEPAGE_CATEGORY_NAME - $sectionTitle",
                        CURRENT_SITE, TOKOPEDIA_DIGITAL_SUBHOMEPAGE,
                        BUSINESS_UNIT, TRAVEL_ENTERTAINMENT))
    }

    private fun mapToProductCardList(list: List<ProductGridCardItemModel>, startingPosition: Int = 0): List<Any> {
        val products = mutableListOf<Any>()
        for ((index, item) in list.withIndex()) {
            products.add(DataLayer.mapOf(
                    NAME, "square product card widget - ${item.title}",
                    POSITION, index + 1 + startingPosition,
                    ID, item.id,
                    PRICE, 0,
                    CATEGORY, item.product,
                    LIST, "/${item.product}"
            ))
        }
        return products
    }

    fun travelHomepageLegoImpression(list: List<LegoBannerItemModel>, componentPosition: Int, sectionTitle: String) {

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_VIEW,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, LEGO_BANNER_IMPRESSION,
                        TrackAppUtils.EVENT_LABEL, "lego banner - $componentPosition - $sectionTitle",
                        CURRENT_SITE, TOKOPEDIA_DIGITAL_SUBHOMEPAGE,
                        BUSINESS_UNIT, TRAVEL_ENTERTAINMENT,
                        ECOMMERCE, DataLayer.mapOf(PROMO_VIEW, DataLayer.mapOf(PROMOTIONS, mapToLegoBannerList(list)))
                ))
    }

    fun travelHomepageLegoClick(item: LegoBannerItemModel, position:Int, componentPosition: Int, sectionTitle: String) {

        if (item.product.isEmpty()) item.product = SUBHOMEPAGE_CATEGORY_NAME
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, LEGO_BANNER_CLICK,
                        TrackAppUtils.EVENT_LABEL, "lego banner - $componentPosition - ${item.product} - $position - ${item.imageUrl}",
                        CURRENT_SITE, TOKOPEDIA_DIGITAL_SUBHOMEPAGE,
                        BUSINESS_UNIT, TRAVEL_ENTERTAINMENT,
                        ECOMMERCE, DataLayer.mapOf(PROMO_CLICK, DataLayer.mapOf(PROMOTIONS, mapToLegoBannerList(listOf(item), position)))
                ))
    }

    private fun mapToLegoBannerList(list: List<LegoBannerItemModel>, startingPosition: Int = 0): List<Any> {
        val products = mutableListOf<Any>()
        for ((index, item) in list.withIndex()) {
            products.add(DataLayer.mapOf(
                    NAME, "lego banner widget - ${item.imageUrl}",
                    POSITION, index + 1 + startingPosition,
                    ID, item.id,
                    CREATIVE, item.imageUrl,
                    CREATIVE_URL, item.imageUrl
            ))
        }
        return products
    }

    fun travelProductCardSliderImpression(list: List<TravelHomepageSectionModel.Item>, componentPosition: Int, sectionTitle: String) {

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PRODUCT_VIEW,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, PRODUCT_CARD_IMPRESSION,
                        TrackAppUtils.EVENT_LABEL, "product card - $componentPosition - $SUBHOMEPAGE_CATEGORY_NAME - $sectionTitle",
                        CURRENT_SITE, TOKOPEDIA_DIGITAL_SUBHOMEPAGE,
                        BUSINESS_UNIT, TRAVEL_ENTERTAINMENT,
                        ECOMMERCE, DataLayer.mapOf(CURRENCY_CODE, IDR_DEFAULT_CURRENCY,
                        IMPRESSIONS, mapToProductSliderList(list))
                ))
    }

    fun travelSliderProductCardClick(item: TravelHomepageSectionModel.Item, position: Int, componentPosition: Int, sectionTitle: String) {

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PRODUCT_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, PRODUCT_CARD_CLICK,
                        TrackAppUtils.EVENT_LABEL, "product card - $componentPosition - ${item.product} - ${position + 1} - $sectionTitle",
                        CURRENT_SITE, TOKOPEDIA_DIGITAL_SUBHOMEPAGE,
                        BUSINESS_UNIT, TRAVEL_ENTERTAINMENT,
                        ECOMMERCE, DataLayer.mapOf(CLICK, DataLayer.mapOf(ACTION_FIELD, DataLayer.mapOf(LIST, "/${item.product}"),
                        PRODUCTS, mapToProductSliderList(listOf(item), position)))
                ))
    }

    fun travelHomepageClickSeeAllSliderProductCard(componentPosition: Int, sectionTitle: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, CLICK_EVENT,
                        TrackAppUtils.EVENT_CATEGORY, TRAVEL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, PRODUCT_CARD_CLICK_SEE_ALL,
                        TrackAppUtils.EVENT_LABEL, "product card - $componentPosition - $sectionTitle",
                        CURRENT_SITE, TOKOPEDIA_DIGITAL_SUBHOMEPAGE,
                        BUSINESS_UNIT, TRAVEL_ENTERTAINMENT))
    }

    private fun mapToProductSliderList(list: List<TravelHomepageSectionModel.Item>, startingPosition: Int = 0): List<Any> {
        val products = mutableListOf<Any>()
        for ((index, item) in list.withIndex()) {
            products.add(DataLayer.mapOf(
                    NAME, "product card widget - ${item.title}",
                    POSITION, index + 1 + startingPosition,
                    ID, item.id,
                    PRICE, 0,
                    CATEGORY, item.product,
                    LIST, "/${item.product}"
            ))
        }
        return products
    }

}