package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * @author by yoasfs on 09/06/20
 */
object MixLeftComponentTracking: BaseTracking()  {

    private object CustomEvent{
        const val CLICK_HOMEPAGE = "clickHomepage"
        const val FORMAT_4_VALUE_UNDERSCORE = "%s_%s_%s_%s";
    }

    private const val LIST_MIX_LEFT = "dynamic channel left carousel"
    private const val IMPRESSION_MIX_LEFT = "impression on product dynamic channel left carousel"
    private const val IMPRESSION_MIX_LEFT_BANNER = "impression on banner dynamic channel left carousel"
    private const val CLICK_MIX_LEFT_BANNER = "click on banner dynamic channel left carousel"
    private const val CLICK_MIX_LEFT = "click on product dynamic channel left carousel"
    private const val PROMOTION_BANNER_ID = "%s_%s_%s_%s"
    private const val PROMOTION_BANNER_NAME = "'/ - p%s - dynamic channel left carousel - banner - %s"


    private const val CLICK_MIX_LEFT_LOADMORE = "click view all on dynamic channel left carousel"
    private const val CLICK_MIX_LEFT_LOADMORE_CARD = "click view all card on dynamic channel left carousel"

    fun getMixLeftClickLoadMore(channel: ChannelModel): HashMap<String, Any> {
        return DataLayer.mapOf(
                Event.KEY, CustomEvent.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, CLICK_MIX_LEFT_LOADMORE,
                Label.KEY, channel.id + " - " + channel.channelHeader.name
        ) as HashMap<String, Any>
    }

    fun getMixLeftClickLoadMoreCard(channel: ChannelModel, userId: String): HashMap<String, Any> {
        return DataLayer.mapOf(
                Event.KEY, CustomEvent.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, CLICK_MIX_LEFT_LOADMORE_CARD,
                Label.KEY, channel.id + " - " + channel.channelHeader.name,
                CurrentSite.KEY, CurrentSite.DEFAULT,
                Screen.KEY, Screen.DEFAULT,
                UserId.KEY, userId,
                BusinessUnit.KEY, BusinessUnit.DEFAULT
        ) as HashMap<String, Any>
    }

    fun getMixLeftProductView(channel: ChannelModel, grid: ChannelGrid, position:Int) = getBasicProductChannelView(
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
                    isFreeOngkir = grid.isFreeOngkirActive,
                    persoType = channel.trackingAttributionModel.persoType,
                    categoryId = channel.trackingAttributionModel.categoryId,
                    isTopAds = grid.isTopads
            )),
            list = String.format(
                    Value.LIST_WITH_HEADER, "1", LIST_MIX_LEFT, channel.channelHeader.name
            ),
            channelId = channel.id
    )

    fun getMixLeftIrisProductView(channel: ChannelModel) = getBasicProductChannelView(
            event = Event.PRODUCT_VIEW_IRIS,
            eventCategory = Category.HOMEPAGE,
            eventAction = IMPRESSION_MIX_LEFT,
            eventLabel = channel.id + " - " + channel.channelHeader.name,
            products = channel.channelGrids.mapIndexed { index, grid ->
                Product(
                        name = grid.name,
                        id = grid.id,
                        productPrice = convertRupiahToInt(
                                grid.price
                        ).toString(),
                        brand = Value.NONE_OTHER,
                        category = Value.NONE_OTHER,
                        variant = Value.NONE_OTHER,
                        productPosition = (index + 1).toString(),
                        channelId = channel.id,
                        isFreeOngkir = grid.isFreeOngkirActive,
                        persoType = channel.trackingAttributionModel.persoType,
                        categoryId = channel.trackingAttributionModel.categoryId,
                        isTopAds = grid.isTopads
                )
            },
            list = String.format(
                    Value.LIST_WITH_HEADER, "1", LIST_MIX_LEFT, channel.channelHeader.name
            ),
            channelId = channel.id
    )

    fun getMixLeftProductClick(channel: ChannelModel, grid: ChannelGrid, position: Int) = getBasicProductChannelClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CLICK_MIX_LEFT,
            eventLabel = channel.id +  " - " + channel.channelHeader.name,
            channelId = channel.id,
            campaignCode = channel.trackingAttributionModel.campaignCode,
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
                            isFreeOngkir = grid.isFreeOngkirActive,
                            persoType = channel.trackingAttributionModel.persoType,
                            categoryId = channel.trackingAttributionModel.categoryId,
                            isTopAds = grid.isTopads
                    )
            ),
            list = String.format(
                    Value.LIST_WITH_HEADER, "1", LIST_MIX_LEFT, channel.channelHeader.name
            )
    )

    fun getMixLeftBannerView(channel: ChannelModel, position: Int) = getBasicPromotionView(
            event = Event.PROMO_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = IMPRESSION_MIX_LEFT_BANNER,
            eventLabel = Label.NONE,
            screen = Screen.DEFAULT,
            currentSite = CurrentSite.DEFAULT,
            businessUnit = BusinessUnit.DEFAULT,
            promotions = listOf(
                    Promotion(
                            id = CustomEvent.FORMAT_4_VALUE_UNDERSCORE.format(channel.id, channel.channelBanner.id, channel.trackingAttributionModel.persoType, channel.trackingAttributionModel.categoryPersona),
                            creative = channel.channelBanner.attribution,
                            name = PROMOTION_BANNER_NAME.format("1", channel.channelHeader.name),
                            position = position.toString()
                    )
            )
    )

    fun getMixLeftBannerClick(channel: ChannelModel, position: Int) = getBasicPromotionChannelClick(
            event = Event.PROMO_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CLICK_MIX_LEFT_BANNER,
            campaignCode = channel.trackingAttributionModel.campaignCode,
            eventLabel = channel.id + " - " + channel.channelHeader.name,
            channelId = channel.id,
            categoryId = channel.trackingAttributionModel.categoryId,
            affinity = channel.trackingAttributionModel.persona,
            attribution = channel.trackingAttributionModel.galaxyAttribution,
            shopId = channel.trackingAttributionModel.brandId,
            screen = Screen.DEFAULT,
            currentSite = CurrentSite.DEFAULT,
            businessUnit = BusinessUnit.DEFAULT,
            promotions = listOf(
                    Promotion(
                            id = CustomEvent.FORMAT_4_VALUE_UNDERSCORE.format(channel.id, channel.channelBanner.id, channel.channelBanner.attribution, channel.trackingAttributionModel.categoryPersona),
                            creative = channel.channelBanner.attribution,
                            name = PROMOTION_BANNER_NAME.format("1", channel.channelHeader.name),
                            position = position.toString()
                    )
            )
    )

    fun sendMixLeftProductClick(channel: ChannelModel, grid: ChannelGrid, position: Int) {
        getTracker().sendEnhanceEcommerceEvent(getMixLeftProductClick(channel, grid, position))
    }

    fun sendMixLeftSeeAllCardClick(channel: ChannelModel, userId: String){
        getTracker().sendEnhanceEcommerceEvent(getMixLeftClickLoadMoreCard(channel, userId))
    }

    fun sendMixLeftSeeAllClick(channel: ChannelModel, userId: String) {
        getTracker().sendEnhanceEcommerceEvent(getMixLeftClickLoadMore(channel))
    }

    fun sendMixLeftBannerClick(channel: ChannelModel, position: Int){
        getTracker().sendEnhanceEcommerceEvent(getMixLeftBannerClick(channel, position))
    }

}