package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer

/**
 * @author by yoasfs on 25/08/20
 */
//need to be deleted after homePageTrackingv2 can implement BaseTrackerConst
class BaseTrackingBuilder: BaseTracking(), BaseTrackingBuilderInterface {

    private var dataLayer: MutableMap<String, Any> = mutableMapOf()

    override fun constructBasicPromotionView(
            event: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String,
            promotions: List<Promotion>
    ): BaseTrackingBuilderInterface {
        dataLayer = DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                Ecommerce.KEY, Ecommerce.getEcommercePromoView(promotions))
        return this
    }

    override fun constructBasicProductView(
            event: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String,
            list: String,
            products: List<Product>
    ): BaseTrackingBuilderInterface {
        dataLayer = DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                Ecommerce.KEY, Ecommerce.getEcommerceProductView(products, list))
        return this
    }

    override fun constructBasicPromotionClick(
            event: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String,
            promotions: List<Promotion>
    ): BaseTrackingBuilderInterface {
        dataLayer = DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                Ecommerce.KEY, Ecommerce.getEcommercePromoClick(promotions))
        return this
    }

    override fun constructBasicProductClick(
            event: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String,
            list: String,
            products: List<Product>
    ): BaseTrackingBuilderInterface {
        dataLayer = DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                Ecommerce.KEY, Ecommerce.getEcommerceProductClick(products, list))
        return this
    }

    override fun appendUserId(value: String): BaseTrackingBuilderInterface {
        dataLayer[UserId.KEY] = value
        return this
    }

    override fun appendChannelId(value: String): BaseTrackingBuilderInterface{
        dataLayer[ChannelId.KEY] = value
        return this
    }

    override fun appendCampaignCode(value: String): BaseTrackingBuilderInterface {
        dataLayer[CampaignCode.KEY] = value
        return this
    }

    override fun appendScreen(value: String): BaseTrackingBuilderInterface {
        dataLayer[Screen.KEY] = value
        return this
    }

    override fun appendCurrentSite(value: String): BaseTrackingBuilderInterface {
        dataLayer[CurrentSite.KEY] = value
        return this
    }

    override fun appendBusinessUnit(value: String): BaseTrackingBuilderInterface {
        dataLayer[BusinessUnit.KEY] = value
        return this
    }

    override fun appendAffinity(value: String): BaseTrackingBuilderInterface {
        dataLayer[Label.AFFINITY_LABEL] = value
        return this
    }

    override fun appendAttribution(value: String): BaseTrackingBuilderInterface {
        dataLayer[Label.ATTRIBUTION_LABEL] = value
        return this
    }

    override fun appendCategoryId(value: String): BaseTrackingBuilderInterface {
        dataLayer[Label.CATEGORY_LABEL] = value
        return this
    }

    override fun appendShopId(value: String): BaseTrackingBuilderInterface {
        dataLayer[Label.SHOP_LABEL] = value
        return this
    }

    override fun build(): Map<String, Any> = dataLayer
}