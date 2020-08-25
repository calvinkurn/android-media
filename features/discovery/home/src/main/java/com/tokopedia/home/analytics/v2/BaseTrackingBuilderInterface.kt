package com.tokopedia.home.analytics.v2

/**
 * @author by yoasfs on 25/08/20
 */

interface BaseTrackingBuilderInterface {

    fun constructBasicPromotionView(event: String,
                                eventCategory: String,
                                eventAction: String,
                                eventLabel: String,
                                promotions: List<BaseTracking.Promotion>,
                                userId: String
    ): BaseTrackingBuilderInterface

    fun constructBasicProductView(event: String,
                              eventCategory: String,
                              eventAction: String,
                              eventLabel: String,
                              list: String,
                              products: List<BaseTracking.Product>,
                              userId: String
    ): BaseTrackingBuilderInterface

    fun constructBasicPromotionClick(event: String,
                                eventCategory: String,
                                eventAction: String,
                                eventLabel: String,
                                promotions: List<BaseTracking.Promotion>,
                                userId: String
    ): BaseTrackingBuilderInterface

    fun constructBasicProductClick(event: String,
                              eventCategory: String,
                              eventAction: String,
                              eventLabel: String,
                              list: String,
                              products: List<BaseTracking.Product>,
                              userId: String
    ): BaseTrackingBuilderInterface

    fun appendChannelId(value: String): BaseTrackingBuilderInterface

    fun appendCampaignCode(value: String): BaseTrackingBuilderInterface

    fun appendScreen(value: String): BaseTrackingBuilderInterface

    fun appendCurrentSite(value: String): BaseTrackingBuilderInterface

    fun appendBusinessUnit(value: String): BaseTrackingBuilderInterface

    fun appendAffinity(value: String): BaseTrackingBuilderInterface

    fun appendAttribution(value: String): BaseTrackingBuilderInterface

    fun appendCategoryId(value: String): BaseTrackingBuilderInterface

    fun appendShopId(value: String): BaseTrackingBuilderInterface

    fun build(): Map<String, Any>
}