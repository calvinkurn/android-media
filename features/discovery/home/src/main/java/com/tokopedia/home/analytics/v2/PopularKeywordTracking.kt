package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.analytics.HomePageTrackingV2
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordDataModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * @author by yoasfs on 08/07/20
 */
object PopularKeywordTracking: BaseTrackerConst() {
    private object CustomEvent{
        const val CLICK_HOMEPAGE = "clickHomepage"
        const val FORMAT_4_VALUE_UNDERSCORE = "%s_%s_%s_%s";
    }
    private const val CLICK_POPULAR_KEYWORDS = "click on popular keyword banner"
    private const val CLICK_POPULAR_KEYWORDS_RELOAD = "click view all on popular keyword banner"
    private const val IMPRESSION_POPULAR_KEYWORDS = "impression on popular keyword banner"
    private const val POPULAR_KEYWORDS_NAME = "popular keyword banner"

    fun getPopularKeywordImpressionItem(
            channel: DynamicHomeChannel.Channels,
            position: Int, keyword: String,
            positionInWidget: Int) : Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder
                .constructBasicPromotionView(
                        event = Event.PROMO_VIEW,
                        eventCategory = Category.HOMEPAGE,
                        eventAction = IMPRESSION_POPULAR_KEYWORDS,
                        eventLabel = channel.header.name,
                        promotions = listOf(
                                Promotion(
                                        id = channel.id,
                                        creative = channel.name,
                                        name = Ecommerce.PROMOTION_NAME.format(positionInWidget, POPULAR_KEYWORDS_NAME, keyword),
                                        position = (position + 1).toString()
                                ))
                )
                .build()
    }

    fun getPopularKeywordImpressionIris(
            channel: DynamicHomeChannel.Channels,
            popularKeywordList: MutableList<PopularKeywordDataModel>,
            positionInWidget: Int) : Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder
                .constructBasicPromotionView(
                        event = Event.PROMO_VIEW_IRIS,
                        eventCategory = Category.HOMEPAGE,
                        eventAction = IMPRESSION_POPULAR_KEYWORDS,
                        eventLabel = channel.header.name,
                        promotions = popularKeywordList.mapIndexed{ pos, data ->
                            Promotion(
                                    id = channel.id,
                                    creative = channel.name,
                                    name = Ecommerce.PROMOTION_NAME.format(positionInWidget, POPULAR_KEYWORDS_NAME, data.title),
                                    position = (pos + 1).toString()
                            )

                        })
                .appendChannelId(channel.id)
                .build()
    }

    fun getPopularKeywordClickItem(
            channel: DynamicHomeChannel.Channels,
            position: Int, keyword: String,
            positionInWidget: Int): Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder
                .constructBasicPromotionClick(
                    event = Event.PROMO_CLICK,
                    eventCategory = Category.HOMEPAGE,
                    eventAction = CLICK_POPULAR_KEYWORDS,
                    eventLabel = String.format(Label.FORMAT_2_ITEMS, channel.header.name, keyword),
                    promotions = listOf(
                            Promotion(
                                    id = channel.id,
                                    creative = channel.name,
                                    name = Ecommerce.PROMOTION_NAME.format(positionInWidget, POPULAR_KEYWORDS_NAME, keyword),
                                    position = (position + 1).toString()
                            )))
                .appendChannelId(channel.id)
                .appendCategoryId(channel.categoryPersona)
                .appendAffinity(channel.persona)
                .appendAttribution(channel.galaxyAttribution)
                .appendShopId(channel.brandId)
                .appendCampaignCode(channel.campaignCode)
                .build()
    }

    fun sendPopularKeywordClickItem(channel: DynamicHomeChannel.Channels, position: Int, keyword: String, positionInWidget: Int) {
        getTracker().sendEnhanceEcommerceEvent(getPopularKeywordClickItem(channel, position, keyword, positionInWidget))
    }

    fun getPopularKeywordClickReload(channel: DynamicHomeChannel.Channels): HashMap<String, Any> {
        return DataLayer.mapOf(
                Event.KEY, CustomEvent.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, CLICK_POPULAR_KEYWORDS_RELOAD,
                Label.KEY, channel.header.name,
                ChannelId.KEY, channel.id
        ) as HashMap<String, Any>
    }

    fun sendPopularKeywordClickReload(channel: DynamicHomeChannel.Channels) {
        getTracker().sendGeneralEvent(getPopularKeywordClickReload(channel))
    }

}