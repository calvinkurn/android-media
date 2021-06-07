package com.tokopedia.home.analytics.v2


import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.analytics.v2.MixTopTracking.CustomAction.Companion.MIXLEFT_LIST
import com.tokopedia.home.analytics.v2.MixTopTracking.CustomAction.Companion.NONTOPADS
import com.tokopedia.home.analytics.v2.MixTopTracking.CustomAction.Companion.TOPADS
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object MixTopTracking : BaseTrackerConst() {
    private class CustomAction{
        companion object {
            const val MIXLEFT_LIST = "/ - p%s - %s - %s - carousel - %s - %s - %s"
            val IMPRESSION_ON_CAROUSEL_PRODUCT = Action.IMPRESSION_ON.format("product dynamic channel top carousel")
            val CLICK_ON_CAROUSEL_PRODUCT = Action.CLICK_ON.format("product dynamic channel top carousel")
            const val CLICK_VIEW_ALL_CAROUSEL = "click view all on dynamic channel top carousel"
            const val CLICK_VIEW_ALL_CAROUSEL_CARD = "click view all card on dynamic channel top carousel"
            const val CLICK_BUTTON_CAROUSEL = "click %s on dynamic channel top carousel"
            const val CLICK_BACKGROUND = "click on background dynamic channel top carousel"
            const val TOPADS = "topads"
            const val NONTOPADS = "non topads"
        }
    }

    private class CustomActionField{
        companion object {
            val LIST_CAROUSEL_PRODUCT = MIXLEFT_LIST.format("%s", "dynamic channel top carousel - product", "%s","%s","%s","%s")
            /// - p{x} - dynamic channel top carousel - product - {topads/non topads} - {carousel/non carousel} - {recommendation_type} - {recomm_page_name} - {header name}
        }
    }

    fun getMixTopView(grid: ChannelGrid, headerName: String, products: List<Product>, positionOnWidgetHome: String): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        val topadsString = grid.getTopadsString()
        return trackingBuilder.constructBasicProductView(
                event = Event.PRODUCT_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = CustomAction.IMPRESSION_ON_CAROUSEL_PRODUCT,
                eventLabel = Label.NONE,
                list = CustomActionField.LIST_CAROUSEL_PRODUCT.format(positionOnWidgetHome, topadsString, grid.recommendationType, "recom_page_name", headerName),
                products = products)
                .appendScreen(Screen.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build()
    }


    fun getMixTopViewIris(grid: ChannelGrid, products: List<Product>, headerName: String, channelId: String, positionOnWidgetHome: String): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        val topadsString = grid.getTopadsString()
        return trackingBuilder.constructBasicProductView(
                event = Event.PRODUCT_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = CustomAction.IMPRESSION_ON_CAROUSEL_PRODUCT,
                eventLabel = Label.NONE,
                list = CustomActionField.LIST_CAROUSEL_PRODUCT.format(positionOnWidgetHome, topadsString, grid.recommendationType, "recom_page_name", headerName),
                products = products)
                .appendScreen(Screen.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendChannelId(channelId)
                .build()
    }

    fun getMixTopClick(grid: ChannelGrid, products: List<Product>, headerName: String, channelId: String, positionOnWidgetHome: String, campaignCode: String): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        val topadsString = grid.getTopadsString()
        return trackingBuilder.constructBasicProductClick(
                event = Event.PRODUCT_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = CustomAction.CLICK_ON_CAROUSEL_PRODUCT,
                eventLabel = "$channelId - $headerName",
                list = CustomActionField.LIST_CAROUSEL_PRODUCT.format(positionOnWidgetHome, topadsString, grid.recommendationType, "recom_page_name", headerName),
                products = products)
                .appendChannelId(channelId)
                .appendCampaignCode(campaignCode)
                .appendScreen(Screen.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build()
    }

    fun getMixTopSeeAllClick(channelId: String, headerName: String, userId: String) = DataLayer.mapOf(
            Event.KEY, Event.CLICK_HOMEPAGE,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CustomAction.CLICK_VIEW_ALL_CAROUSEL,
            Label.KEY, "$channelId - $headerName",
            ChannelId.KEY, channelId,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            Screen.KEY, Screen.DEFAULT,
            UserId.KEY, userId,
            BusinessUnit.KEY, BusinessUnit.DEFAULT

    )

    fun getMixTopSeeAllCardClick(channelId: String, headerName: String, userId: String) = DataLayer.mapOf(
            Event.KEY, Event.CLICK_HOMEPAGE,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CustomAction.CLICK_VIEW_ALL_CAROUSEL_CARD,
            Label.KEY, "$channelId - $headerName",
            Screen.KEY, Screen.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            Screen.KEY, Screen.DEFAULT,
            UserId.KEY, userId,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            ChannelId.KEY, channelId
    )

    fun getMixTopButtonClick(channelId: String,headerName: String, buttonName: String, userId: String) = DataLayer.mapOf(
            Event.KEY, Event.CLICK_HOMEPAGE,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CustomAction.CLICK_BUTTON_CAROUSEL.format(buttonName),
            Label.KEY, "$channelId - $headerName",
            ChannelId.KEY, channelId,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            Screen.KEY, Screen.DEFAULT,
            UserId.KEY, userId,
            BusinessUnit.KEY, BusinessUnit.DEFAULT
    )

    private fun mapGridToProductTracker(grid: DynamicHomeChannel.Grid, channelId: String, position: Int, persoType: String, categoryId: String) = Product(
            id = grid.id,
            name = grid.name,
            brand = "",
            category = "",
            channelId = channelId,
            isFreeOngkir = grid.freeOngkir.isActive,
            productPosition = position.toString(),
            productPrice = convertRupiahToInt(grid.price).toString(),
            variant = "",
            persoType = persoType,
            categoryId = categoryId,
            isTopAds = grid.isTopads
    )

    fun mapChannelToProductTracker(channels: DynamicHomeChannel.Channels) = channels.grids.withIndex().map {
        mapGridToProductTracker(it.value, channels.id, it.index, channels.persoType, channels.categoryID)
    }

    //home component section

    fun mapChannelToProductTracker(channels: ChannelModel) = channels.channelGrids.withIndex().map {
        mapGridToProductTrackerComponent(it.value, channels.id, it.index, channels.trackingAttributionModel.persoType, channels.trackingAttributionModel.categoryId, channels.channelHeader.name)
    }

    fun mapGridToProductTrackerComponent(grid: ChannelGrid, channelId: String, position: Int, persoType: String, categoryId: String, headerName: String = "", pageName: String = "") = Product(
            id = grid.id,
            name = grid.name,
            brand = "",
            category = "",
            channelId = channelId,
            isFreeOngkir = grid.isFreeOngkirActive && !grid.labelGroup.hasLabelGroupFulfillment(),
            isFreeOngkirExtra = grid.isFreeOngkirActive && grid.labelGroup.hasLabelGroupFulfillment(),
            productPosition = position.toString(),
            productPrice = convertRupiahToInt(grid.price).toString(),
            variant = "",
            persoType = persoType,
            categoryId = categoryId,
            isTopAds = grid.isTopads,
            recommendationType = grid.recommendationType,
            pageName = pageName,
            isCarousel = true,
            headerName = headerName
    )

    fun getBackgroundClickComponent(channels: ChannelModel, userId: String = "") = DataLayer.mapOf(
            Event.KEY, Event.CLICK_HOMEPAGE,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CustomAction.CLICK_BACKGROUND,
            Label.KEY, channels.id + " - " + channels.channelHeader.name,
            Screen.KEY, Screen.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            Screen.KEY, Screen.DEFAULT,
            UserId.KEY, userId,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            ChannelId.KEY, channels.id,
            CampaignCode.KEY, channels.trackingAttributionModel.campaignCode,
            Label.ATTRIBUTION_LABEL, channels.channelBanner.attribution,
            Label.AFFINITY_LABEL, channels.trackingAttributionModel.persona,
            Label.CATEGORY_LABEL, channels.trackingAttributionModel.categoryId,
            Label.SHOP_LABEL, channels.trackingAttributionModel.brandId
    )

    //end of home component section

    private fun ChannelGrid.getTopadsString(): String {
        return if (this.isTopads) TOPADS else NONTOPADS
    }

}