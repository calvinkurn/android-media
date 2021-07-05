package com.tokopedia.track.builder

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.track.builder.`interface`.BaseTrackerBuilderInterface
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * @author by yoasfs on 01/09/20
 */
class BaseTrackerBuilder : BaseTrackerConst(), BaseTrackerBuilderInterface{

    private var dataLayer: MutableMap<String, Any> = mutableMapOf()

    override fun constructBasicPromotionView(event: String,
                                             eventCategory: String,
                                             eventAction: String,
                                             eventLabel: String,
                                             promotions: List<Promotion>): BaseTrackerBuilderInterface {
        dataLayer = DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                Ecommerce.KEY, Ecommerce.getEcommercePromoView(promotions))
        return this
    }

    override fun constructBasicProductView(event: String,
                                           eventCategory: String,
                                           eventAction: String,
                                           eventLabel: String,
                                           list: String,
                                           products: List<Product>,
                                           buildCustomList: ((Product) -> String)?
    ): BaseTrackerBuilderInterface {
        dataLayer = DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                Ecommerce.KEY, Ecommerce.getEcommerceProductView(products, list, buildCustomList))
        return this
    }

    override fun constructBasicPromotionClick(event: String,
                                              eventCategory: String,
                                              eventAction: String,
                                              eventLabel: String,
                                              promotions: List<Promotion>): BaseTrackerBuilderInterface {
        dataLayer = DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                Ecommerce.KEY, Ecommerce.getEcommercePromoClick(promotions))
        return this
    }

    override fun constructBasicProductClick(event: String,
                                            eventCategory: String,
                                            eventAction: String,
                                            eventLabel: String,
                                            list: String,
                                            products: List<Product>,
                                            buildCustomList: ((Product) -> String)?): BaseTrackerBuilderInterface {
        dataLayer = DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                Ecommerce.KEY, Ecommerce.getEcommerceProductClick(products, list, buildCustomList))
        return this
    }

    override fun constructBasicGeneralClick(event: String, eventCategory: String, eventAction: String, eventLabel: String): BaseTrackerBuilderInterface {
        dataLayer = DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel)
        return this
    }

    override fun appendEvent(value: String): BaseTrackerBuilderInterface {
        dataLayer[Event.KEY] = value
        return this
    }

    override fun appendEventCategory(value: String): BaseTrackerBuilderInterface {
        dataLayer[Category.KEY] = value
        return this
    }

    override fun appendEventAction(value: String): BaseTrackerBuilderInterface {
        dataLayer[Action.KEY] = value
        return this
    }

    override fun appendEventLabel(value: String): BaseTrackerBuilderInterface {
        dataLayer[Label.KEY] = value
        return this
    }

    override fun appendUserId(value: String): BaseTrackerBuilderInterface {
        dataLayer[UserId.KEY] = value
        return this
    }

    override fun appendChannelId(value: String): BaseTrackerBuilderInterface {
        dataLayer[ChannelId.KEY] = value
        return this
    }

    override fun appendCampaignCode(value: String): BaseTrackerBuilderInterface {
        dataLayer[CampaignCode.KEY] = value
        return this
    }

    override fun appendScreen(value: String): BaseTrackerBuilderInterface {
        dataLayer[Screen.KEY] = value
        return this
    }

    override fun appendCurrentSite(value: String): BaseTrackerBuilderInterface {
        dataLayer[CurrentSite.KEY] = value
        return this
    }

    override fun appendBusinessUnit(value: String): BaseTrackerBuilderInterface {
        dataLayer[BusinessUnit.KEY] = value
        return this
    }

    override fun appendAffinity(value: String): BaseTrackerBuilderInterface {
        dataLayer[Label.AFFINITY_LABEL] = value
        return this
    }

    override fun appendAttribution(value: String): BaseTrackerBuilderInterface {
        dataLayer[Label.ATTRIBUTION_LABEL] = value
        return this
    }

    override fun appendCategoryId(value: String): BaseTrackerBuilderInterface {
        dataLayer[Label.CATEGORY_LABEL] = value
        return this
    }

    override fun appendShopId(value: String): BaseTrackerBuilderInterface {
        dataLayer[Label.SHOP_LABEL] = value
        return this
    }

    override fun appendCustomKeyValue(key: String, value: Any): BaseTrackerBuilderInterface {
        dataLayer[key] = value
        return this
    }

    override fun build(): Map<String, Any> {
        return dataLayer
    }
}