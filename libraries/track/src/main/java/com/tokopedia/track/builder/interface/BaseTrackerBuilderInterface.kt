package com.tokopedia.track.builder.`interface`

import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * @author by yoasfs on 01/09/20
 */
interface BaseTrackerBuilderInterface {
    fun constructBasicPromotionView(event: String,
                                    eventCategory: String,
                                    eventAction: String,
                                    eventLabel: String,
                                    promotions: List<BaseTrackerConst.Promotion>
    ): BaseTrackerBuilderInterface

    fun constructBasicProductView(event: String,
                                  eventCategory: String,
                                  eventAction: String,
                                  eventLabel: String,
                                  list: String,
                                  products: List<BaseTrackerConst.Product>,
                                  buildCustomList: ((BaseTrackerConst.Product) -> String)? = null
    ): BaseTrackerBuilderInterface

    fun constructBasicPromotionClick(event: String,
                                     eventCategory: String,
                                     eventAction: String,
                                     eventLabel: String,
                                     promotions: List<BaseTrackerConst.Promotion>
    ): BaseTrackerBuilderInterface

    fun constructBasicProductClick(event: String,
                                   eventCategory: String,
                                   eventAction: String,
                                   eventLabel: String,
                                   list: String,
                                   products: List<BaseTrackerConst.Product>,
                                   buildCustomList: ((BaseTrackerConst.Product) -> String)? = null
    ): BaseTrackerBuilderInterface

    fun constructBasicGeneralClick(event: String,
                                   eventCategory: String,
                                   eventAction: String,
                                   eventLabel: String
    ): BaseTrackerBuilderInterface

    fun appendEvent(value: String): BaseTrackerBuilderInterface
    fun appendEventCategory(value: String): BaseTrackerBuilderInterface
    fun appendEventAction(value: String): BaseTrackerBuilderInterface
    fun appendEventLabel(value: String): BaseTrackerBuilderInterface

    fun appendUserId(value: String): BaseTrackerBuilderInterface
    fun appendChannelId(value: String): BaseTrackerBuilderInterface
    fun appendCampaignCode(value: String): BaseTrackerBuilderInterface
    fun appendScreen(value: String): BaseTrackerBuilderInterface
    fun appendCurrentSite(value: String): BaseTrackerBuilderInterface
    fun appendBusinessUnit(value: String): BaseTrackerBuilderInterface
    fun appendAffinity(value: String): BaseTrackerBuilderInterface
    fun appendAttribution(value: String): BaseTrackerBuilderInterface
    fun appendCategoryId(value: String): BaseTrackerBuilderInterface
    fun appendShopId(value: String): BaseTrackerBuilderInterface

    fun appendCustomKeyValue(key: String, value: Any): BaseTrackerBuilderInterface

    fun build(): Map<String, Any>
}