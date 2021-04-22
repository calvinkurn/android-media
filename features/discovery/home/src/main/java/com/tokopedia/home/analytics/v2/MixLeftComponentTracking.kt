package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * @author by yoasfs on 09/06/20
 */
object MixLeftComponentTracking: BaseTrackerConst()  {

    private object CustomEvent{
        const val CLICK_HOMEPAGE = "clickHomepage"
        const val FORMAT_4_VALUE_UNDERSCORE = "%s_%s_%s_%s";
    }

    private const val LIST_MIX_LEFT = "dynamic channel left carousel - product"
    private const val IMPRESSION_MIX_LEFT = "impression on product dynamic channel left carousel"
    private const val IMPRESSION_MIX_LEFT_BANNER = "impression on banner dynamic channel left carousel"
    private const val CLICK_MIX_LEFT_BANNER = "click on banner dynamic channel left carousel"
    private const val CLICK_MIX_LEFT = "click on product dynamic channel left carousel"
    private const val PROMOTION_BANNER_ID = "%s_%s_%s_%s"
    private const val PROMOTION_BANNER_NAME = "/ - p%s - dynamic channel left carousel - banner - %s"


    private const val CLICK_MIX_LEFT_LOADMORE = "click view all on dynamic channel left carousel"
    private const val CLICK_MIX_LEFT_LOADMORE_CARD = "click view all card on dynamic channel left carousel"


    private fun getMixLeftClickLoadMore(channel: ChannelModel, userId: String): HashMap<String, Any> {
        return DataLayer.mapOf(
                Event.KEY, CustomEvent.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, CLICK_MIX_LEFT_LOADMORE,
                Label.KEY, channel.id + " - " + channel.channelHeader.name,
                ChannelId.KEY, channel.id,
                Screen.KEY, Screen.DEFAULT,
                CurrentSite.KEY, CurrentSite.DEFAULT,
                UserId.KEY, userId,
                BusinessUnit.KEY, BusinessUnit.DEFAULT
        ) as HashMap<String, Any>
    }

    private fun getMixLeftClickLoadMoreCard(channel: ChannelModel, userId: String): HashMap<String, Any> {
        return DataLayer.mapOf(
                Event.KEY, CustomEvent.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, CLICK_MIX_LEFT_LOADMORE_CARD,
                Label.KEY, channel.id + " - " + channel.channelHeader.name,
                CurrentSite.KEY, CurrentSite.DEFAULT,
                ChannelId.KEY, channel.id,
                Screen.KEY, Screen.DEFAULT,
                UserId.KEY, userId,
                BusinessUnit.KEY, BusinessUnit.DEFAULT
        ) as HashMap<String, Any>
    }

    fun getMixLeftProductView(channel: ChannelModel, grid: ChannelGrid, position:Int, positionOnHome: Int): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        return trackingBuilder.constructBasicProductView(
                event = Event.PRODUCT_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = IMPRESSION_MIX_LEFT,
                eventLabel = channel.id + " - " + channel.channelHeader.name,
                products =  listOf(Product(
                        name = grid.name,
                        id = grid.id,
                        productPrice = convertRupiahToInt(
                                grid.price
                        ).toString(),
                        brand = Value.NONE_OTHER,
                        category = Value.NONE_OTHER,
                        variant = Value.NONE_OTHER,
                        productPosition = (position + 1).toString(),
                        channelId = channel.id,
                        isFreeOngkir = grid.isFreeOngkirActive && !grid.labelGroup.hasLabelGroupFulfillment(),
                        isFreeOngkirExtra = grid.isFreeOngkirActive && grid.labelGroup.hasLabelGroupFulfillment(),
                        persoType = channel.trackingAttributionModel.persoType,
                        categoryId = channel.trackingAttributionModel.categoryId,
                        isTopAds = grid.isTopads,
                        recommendationType = grid.recommendationType,
                        headerName = channel.channelHeader.name,
                        pageName = channel.pageName,
                        isCarousel = true
                )),
                list = String.format(
                        Value.LIST, positionOnHome, LIST_MIX_LEFT
                ))
                .appendChannelId(channel.id)
                .build()
    }

    fun getMixLeftIrisProductView(channel: ChannelModel, grid: ChannelGrid, position:Int, positionOnHome: Int): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        return trackingBuilder.constructBasicProductView(
                event = Event.PRODUCT_VIEW_IRIS,
                eventCategory = Category.HOMEPAGE,
                eventAction = IMPRESSION_MIX_LEFT,
                eventLabel = channel.id + " - " + channel.channelHeader.name,
                products =  listOf(Product(
                        name = grid.name,
                        id = grid.id,
                        productPrice = convertRupiahToInt(
                                grid.price
                        ).toString(),
                        brand = Value.NONE_OTHER,
                        category = Value.NONE_OTHER,
                        variant = Value.NONE_OTHER,
                        productPosition = (position + 1).toString(),
                        channelId = channel.id,
                        isFreeOngkir = grid.isFreeOngkirActive && !grid.labelGroup.hasLabelGroupFulfillment(),
                        isFreeOngkirExtra = grid.isFreeOngkirActive && grid.labelGroup.hasLabelGroupFulfillment(),
                        persoType = channel.trackingAttributionModel.persoType,
                        categoryId = channel.trackingAttributionModel.categoryId,
                        isTopAds = grid.isTopads,
                        recommendationType = grid.recommendationType,
                        headerName = channel.channelHeader.name,
                        pageName = channel.pageName,
                        isCarousel = true
                )),
                list = String.format(
                        Value.LIST, positionOnHome, LIST_MIX_LEFT
                ))
                .appendScreen(Screen.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendChannelId(channel.id)
                .build()
    }

    private fun getMixLeftProductClick(channel: ChannelModel, grid: ChannelGrid, position: Int, positionOnHome: Int) : Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        return trackingBuilder.constructBasicProductClick(
                event = Event.PRODUCT_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = CLICK_MIX_LEFT,
                eventLabel = channel.id +  " - " + channel.channelHeader.name,
                products = listOf(
                        Product(
                                name = grid.name,
                                id = grid.id,
                                productPrice = convertRupiahToInt(
                                        grid.price
                                ).toString(),
                                brand = Value.NONE_OTHER,
                                category = Value.NONE_OTHER,
                                variant = Value.NONE_OTHER,
                                productPosition = (position + 1).toString(),
                                channelId = channel.id,
                                isFreeOngkir = grid.isFreeOngkirActive && !grid.labelGroup.hasLabelGroupFulfillment(),
                                isFreeOngkirExtra = grid.isFreeOngkirActive && grid.labelGroup.hasLabelGroupFulfillment(),
                                persoType = channel.trackingAttributionModel.persoType,
                                categoryId = channel.trackingAttributionModel.categoryId,
                                isTopAds = grid.isTopads,
                                recommendationType = grid.recommendationType,
                                headerName = channel.channelHeader.name,
                                pageName = channel.pageName,
                                isCarousel = true
                        )
                ),
                list = String.format(
                        Value.LIST, positionOnHome, LIST_MIX_LEFT
                ))
                .appendChannelId(channel.id)
                .appendScreen(Screen.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCampaignCode(channel.trackingAttributionModel.campaignCode)
                .build()

    }

    fun getMixLeftBannerView(channel: ChannelModel, position: Int, userId: String): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        return trackingBuilder.constructBasicPromotionView(
                event = Event.PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = IMPRESSION_MIX_LEFT_BANNER,
                eventLabel = Label.NONE,
                promotions = listOf(
                        Promotion(
                                id = CustomEvent.FORMAT_4_VALUE_UNDERSCORE.format(
                                        channel.id,
                                        channel.channelBanner.id,
                                        channel.trackingAttributionModel.persoType,
                                        channel.trackingAttributionModel.categoryId),
                                creative = channel.channelBanner.attribution,
                                name = PROMOTION_BANNER_NAME.format(position, channel.channelHeader.name),
                                position = position.toString()
                        )
                ))
                .appendUserId(userId)
                .appendScreen(Screen.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)

                .build()
    }


    private fun getMixLeftBannerClick(channel: ChannelModel, position: Int, userId: String): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        return trackingBuilder.constructBasicPromotionClick(
                event = Event.PROMO_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = CLICK_MIX_LEFT_BANNER,
                eventLabel = channel.id + " - " + channel.channelHeader.name,
                promotions = listOf(
                        Promotion(
                                id = CustomEvent.FORMAT_4_VALUE_UNDERSCORE.format(
                                        channel.id,
                                        channel.channelBanner.id,
                                        channel.trackingAttributionModel.persoType,
                                        channel.trackingAttributionModel.categoryId),
                                creative = channel.channelBanner.attribution,
                                name = PROMOTION_BANNER_NAME.format(position, channel.channelHeader.name),
                                position = position.toString()
                        )
                ))
                .appendUserId(userId)
                .appendCampaignCode(channel.trackingAttributionModel.campaignCode)
                .appendChannelId(channel.id)
                .appendCategoryId(channel.trackingAttributionModel.categoryId)
                .appendAffinity(channel.trackingAttributionModel.persona)
                .appendAttribution(channel.trackingAttributionModel.galaxyAttribution)
                .appendShopId(channel.trackingAttributionModel.brandId)
                .appendScreen(Screen.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .build()
    }

    fun sendMixLeftProductClick(channel: ChannelModel, grid: ChannelGrid, position: Int, positionOnHome: Int) {
        getTracker().sendEnhanceEcommerceEvent(getMixLeftProductClick(channel, grid, position, positionOnHome))
    }

    fun sendMixLeftSeeAllCardClick(channel: ChannelModel, userId: String){
        getTracker().sendEnhanceEcommerceEvent(getMixLeftClickLoadMoreCard(channel, userId))
    }

    fun sendMixLeftSeeAllClick(channel: ChannelModel, userId: String) {
        getTracker().sendEnhanceEcommerceEvent(getMixLeftClickLoadMore(channel, userId))
    }

    fun sendMixLeftBannerClick(channel: ChannelModel, position: Int,  userId: String){
        getTracker().sendEnhanceEcommerceEvent(getMixLeftBannerClick(channel, position, userId))
    }

}