package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * @author by yoasfs on 03/09/20
 */

object RecommendationListTracking: BaseTrackerConst(){
    private const val RECOMMENDATION_LIST_CAROUSEL_PRODUCT = "dynamic channel list carousel"
    private const val RECOMMENDATION_LIST_IMPRESSION_EVENT_ACTION = "impression on dynamic channel list carousel"
    private const val RECOMMENDATION_LIST_CLICK_EVENT_ACTION = "click on dynamic channel list carousel"
    private const val RECOMMENDATION_LIST_CLICK_ADD_TO_CART_EVENT_ACTION = "click add to cart on dynamic channel list carousel"
    private const val RECOMMENDATION_LIST_SEE_ALL_EVENT_ACTION = "click view all on dynamic channel list carousel"
    private const val RECOMMENDATION_LIST_SEE_ALL_CARD_EVENT_ACTION = "click view all card on dynamic channel list carousel"
    private const val RECOMMENDATION_LIST_CLOSE_EVENT_ACTION = "click on close dynamic channel list carousel"
    private const val LIST_DYNAMIC_CHANNEL_LIST_CAROUSEL = "/ - p%s - dynamic channel list carousel - product - %s - %s - %s - %s"

    fun getRecommendationListImpression(channel: DynamicHomeChannel.Channels, isToIris: Boolean = false, userId: String, parentPosition: Int): Map<String, Any> {
        val baseTracker = BaseTrackerBuilder()
        return baseTracker.constructBasicProductView(event = if (isToIris) Event.PRODUCT_VIEW_IRIS else Event.PRODUCT_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = RECOMMENDATION_LIST_IMPRESSION_EVENT_ACTION,
                eventLabel = Label.NONE,
                products = channel.grids.mapIndexed { index, grid ->
                    Product(
                            name = grid.name,
                            id = grid.id,
                            productPrice = convertRupiahToInt(grid.price).toString(),
                            brand = Value.NONE_OTHER,
                            category = Value.NONE_OTHER,
                            variant = Value.NONE_OTHER,
                            productPosition = (index + 1).toString(),
                            channelId = channel.id,
                            isFreeOngkir = grid.freeOngkir.isActive,
                            persoType = channel.persoType,
                            categoryId = channel.categoryID,
                            isTopAds = grid.isTopads,
                            headerName = channel.header.name,
                            isCarousel = true,
                            pageName = channel.pageName,
                            recommendationType = grid.recommendationType

                    )
                },
                list = String.format(
                        "/ - p%s - %s - product", parentPosition, RECOMMENDATION_LIST_CAROUSEL_PRODUCT
                ))
                .appendChannelId(channel.id)
                .appendUserId(userId)
                .appendScreen(Screen.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .build()
    }

    fun getRecommendationListImpression(channel: ChannelModel, isToIris: Boolean = false, userId: String, parentPosition: Int): Map<String, Any> {
        val baseTracker = BaseTrackerBuilder()
        return baseTracker.constructBasicProductView(
                event = if (isToIris) Event.PRODUCT_VIEW_IRIS else Event.PRODUCT_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = RECOMMENDATION_LIST_IMPRESSION_EVENT_ACTION,
                eventLabel = Label.NONE,
                products = channel.channelGrids.mapIndexed { index, grid ->
                    Product(
                            name = grid.name,
                            id = grid.id,
                            productPrice = convertRupiahToInt(grid.price).toString(),
                            brand = Value.NONE_OTHER,
                            category = Value.NONE_OTHER,
                            variant = Value.NONE_OTHER,
                            productPosition = (index + 1).toString(),
                            channelId = channel.id,
                            isFreeOngkir = grid.isFreeOngkirActive,
                            persoType = channel.trackingAttributionModel.persoType,
                            categoryId = channel.trackingAttributionModel.categoryId,
                            isTopAds = grid.isTopads,
                            headerName = channel.channelHeader.name,
                            isCarousel = true,
                            pageName = channel.pageName,
                            recommendationType = grid.recommendationType
                    )
                },
                list = String.format(
                        "/ - p%s - %s - product", parentPosition, RECOMMENDATION_LIST_CAROUSEL_PRODUCT
                ))
                .appendChannelId(channel.id)
                .appendUserId(userId)
                .appendScreen(Screen.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .build()
    }
    fun getRecommendationListProductImpression(channel: ChannelModel, grid: ChannelGrid, isToIris: Boolean = false, userId: String, itemPosition: Int, parentPosition: Int): Map<String, Any> {
        val baseTracker =  BaseTrackerBuilder()
        return baseTracker.constructBasicProductView(
                event = if (isToIris) Event.PRODUCT_VIEW_IRIS else Event.PRODUCT_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = RECOMMENDATION_LIST_IMPRESSION_EVENT_ACTION,
                eventLabel = Label.NONE,
                products = listOf(
                    Product(
                            name = grid.name,
                            id = grid.id,
                            productPrice = convertRupiahToInt(grid.price).toString(),
                            brand = Value.NONE_OTHER,
                            category = Value.NONE_OTHER,
                            variant = Value.NONE_OTHER,
                            productPosition = (itemPosition + 1).toString(),
                            channelId = channel.id,
                            isFreeOngkir = grid.isFreeOngkirActive,
                            persoType = channel.trackingAttributionModel.persoType,
                            categoryId = channel.trackingAttributionModel.categoryId,
                            isTopAds = grid.isTopads,
                            headerName = channel.channelHeader.name,
                            isCarousel = true,
                            pageName = channel.pageName,
                            recommendationType = grid.recommendationType)),
                list = String.format(
                        "/ - p%s - %s - product", parentPosition, RECOMMENDATION_LIST_CAROUSEL_PRODUCT
                ))
                .appendChannelId(channel.id)
                .appendUserId(userId)
                .appendScreen(Screen.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .build()
    }

    private fun getRecommendationListClickHomeComponent(channel: ChannelModel, grid: ChannelGrid, position: Int, userId: String, parentPosition: Int): Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicProductClick(event = Event.PRODUCT_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = RECOMMENDATION_LIST_CLICK_EVENT_ACTION,
                eventLabel = channel.id + " - " + channel.channelHeader.name,
                products = listOf(
                        Product(
                                name = grid.name,
                                id = grid.id,
                                productPrice = convertRupiahToInt(grid.price).toString(),
                                brand = Value.NONE_OTHER,
                                category = Value.NONE_OTHER,
                                variant = Value.NONE_OTHER,
                                productPosition = (position + 1).toString(),
                                channelId = channel.id,
                                    isFreeOngkir = grid.isFreeOngkirActive,
                                persoType = channel.trackingAttributionModel.persoType,
                                categoryId = channel.trackingAttributionModel.categoryId,
                                isTopAds = grid.isTopads,
                                headerName = channel.channelHeader.name,
                                pageName = channel.pageName,
                                isCarousel = true,
                                recommendationType = grid.recommendationType
                        )
                ),
                list = String.format(
                        "/ - p%s - %s - product", parentPosition, RECOMMENDATION_LIST_CAROUSEL_PRODUCT
                ))
                .appendChannelId(channel.id)
                .appendCampaignCode(channel.trackingAttributionModel.campaignCode)
                .appendUserId(userId)
                .appendScreen(Screen.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .build()
    }

    fun sendRecommendationListHomeComponentClick(channel: ChannelModel, grid: ChannelGrid, position: Int, userId: String, parentPosition: Int) {
        getTracker().sendEnhanceEcommerceEvent(getRecommendationListClickHomeComponent(channel, grid, position, userId, parentPosition))
    }

    private fun getRecommendationListSeeAllClick(channelId: String, headerName: String, userId: String): HashMap<String, Any>{
        return DataLayer.mapOf(
                Event.KEY, Event.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, RECOMMENDATION_LIST_SEE_ALL_EVENT_ACTION,
                Label.KEY, Label.FORMAT_2_ITEMS.format(channelId, headerName),
                Screen.KEY, Screen.DEFAULT,
                UserId.KEY, userId,
                CurrentSite.KEY, CurrentSite.DEFAULT,
                BusinessUnit.KEY, BusinessUnit.DEFAULT,
                ChannelId.KEY, channelId
        ) as HashMap<String, Any>
    }

    private fun getRecommendationListSeeAllCardClick(channelId: String, headerName: String, userId: String): HashMap<String, Any>{
        return DataLayer.mapOf(
                Event.KEY, Event.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, RECOMMENDATION_LIST_SEE_ALL_CARD_EVENT_ACTION,
                Label.KEY, Label.FORMAT_2_ITEMS.format(channelId, headerName),
                Screen.KEY, Screen.DEFAULT,
                UserId.KEY, userId,
                CurrentSite.KEY, CurrentSite.DEFAULT,
                BusinessUnit.KEY, BusinessUnit.DEFAULT,
                ChannelId.KEY, channelId
        ) as HashMap<String, Any>
    }

    fun sendRecommendationListSeeAllClick(channelId: String, headerName: String, userId: String) {
        getTracker().sendGeneralEvent(getRecommendationListSeeAllClick(channelId, headerName, userId))
    }

    fun sendRecommendationListSeeAllCardClick(channelId: String, headerName: String, userId: String) {
        getTracker().sendGeneralEvent(getRecommendationListSeeAllCardClick(channelId, headerName, userId))
    }

    fun getCloseClickOnDynamicListCarouselHomeComponent(channel: ChannelModel, userId: String = "") = DataLayer.mapOf(
            Event.KEY, Event.CLICK_HOMEPAGE,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, RECOMMENDATION_LIST_CLOSE_EVENT_ACTION,
            Label.KEY, Label.FORMAT_2_ITEMS.format(channel.id, channel.channelHeader.name),
            Screen.KEY, Screen.DEFAULT,
            UserId.KEY, userId,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            ChannelId.KEY,channel.id
    )

    fun getAddToCartOnDynamicListCarousel(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int, cartId: String, quantity: String = "0", userId: String = "") = DataLayer.mapOf(
            Event.KEY, Event.PRODUCT_ADD_TO_CART,
            Category.KEY, Category.HOMEPAGE,
            Label.KEY, Label.FORMAT_2_ITEMS.format(channel.id, channel.header.name),
            Action.KEY,RECOMMENDATION_LIST_CLICK_ADD_TO_CART_EVENT_ACTION,
            Label.CAMPAIGN_CODE, channel.campaignCode,
            Screen.KEY, Screen.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            UserId.KEY, userId,
            Label.CHANNEL_LABEL, channel.id,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            Ecommerce.KEY, Ecommerce.getEcommerceProductAddToCart(
            products = listOf(
                    Product(
                            name = grid.name,
                            id = grid.id,
                            productPrice = convertRupiahToInt(grid.price).toString(),
                            brand = Value.NONE_OTHER,
                            category = Value.NONE_OTHER,
                            variant = Value.NONE_OTHER,
                            productPosition = (position + 1).toString(),
                            channelId = channel.id,
                            isFreeOngkir = grid.freeOngkir.isActive,
                            persoType = channel.persoType,
                            categoryId = channel.categoryID,
                            isTopAds = grid.isTopads,
                            quantity = quantity,
                            cartId = cartId,
                            shopId = channel.brandId
                    )
            ),
            list = String.format(
                    "/ - p%s - %s - product", "1", RECOMMENDATION_LIST_CAROUSEL_PRODUCT
            )
    )

    )

    fun getAddToCartOnDynamicListCarouselHomeComponent(channel: ChannelModel, grid: ChannelGrid, position: Int, cartId: String, quantity: String = "0", userId: String = "") = DataLayer.mapOf(
            Event.KEY, Event.PRODUCT_ADD_TO_CART,
            Category.KEY, Category.HOMEPAGE,
            Label.KEY, Label.FORMAT_2_ITEMS.format(channel.id, channel.channelHeader.name),
            Action.KEY,RECOMMENDATION_LIST_CLICK_ADD_TO_CART_EVENT_ACTION,
            Label.CAMPAIGN_CODE, channel.trackingAttributionModel.campaignCode,
            Screen.KEY, Screen.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            UserId.KEY, userId,
            Label.CHANNEL_LABEL, channel.id,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            Ecommerce.KEY, Ecommerce.getEcommerceProductAddToCart(
            products = listOf(
                    Product(
                            name = grid.name,
                            id = grid.id,
                            productPrice = convertRupiahToInt(grid.price).toString(),
                            brand = Value.NONE_OTHER,
                            category = Value.NONE_OTHER,
                            variant = Value.NONE_OTHER,
                            productPosition = (position + 1).toString(),
                            channelId = channel.id,
                            isFreeOngkir = grid.isFreeOngkirActive,
                            persoType = channel.trackingAttributionModel.persoType,
                            categoryId = channel.trackingAttributionModel.categoryId,
                            pageName = channel.pageName,
                            recommendationType = grid.recommendationType,
                            isTopAds = grid.isTopads,
                            quantity = quantity,
                            cartId = cartId,
                            shopId = channel.trackingAttributionModel.brandId,
                            headerName = channel.channelHeader.name
                    )
            ),
            list = String.format(
                    "/ - p%s - %s - product", "1", RECOMMENDATION_LIST_CAROUSEL_PRODUCT
            )
    )

    )
}