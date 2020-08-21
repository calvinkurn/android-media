package com.tokopedia.home.analytics.v2


import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.design.utils.CurrencyFormatHelper
import com.tokopedia.home.analytics.v2.BaseTracking.Value.LIST
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

object MixTopTracking : BaseTracking() {
    private class CustomAction{
        companion object {
            val IMPRESSION_ON_CAROUSEL_PRODUCT = Action.IMPRESSION_ON.format("product dynamic channel top carousel")
            val CLICK_ON_CAROUSEL_PRODUCT = Action.CLICK_ON.format("product dynamic channel top carousel")
            const val CLICK_VIEW_ALL_CAROUSEL = "click view all on dynamic channel top carousel"
            const val CLICK_VIEW_ALL_CAROUSEL_CARD = "click view all card on dynamic channel top carousel"
            const val CLICK_BUTTON_CAROUSEL = "click %s on dynamic channel top carousel"
            const val CLICK_BACKGROUND = "click on background dynamic channel top carousel"
        }
    }

    private class CustomActionField{
        companion object {
            val LIST_CAROUSEL_PRODUCT = LIST.format("%s", "dynamic channel top carousel - %s")
        }
    }

    fun getMixTopView(products: List<Product>, headerName: String, positionOnWidgetHome: String) = getBasicProductView(
            Event.PRODUCT_VIEW,
            Category.HOMEPAGE,
            CustomAction.IMPRESSION_ON_CAROUSEL_PRODUCT,
            Label.NONE,
            CustomActionField.LIST_CAROUSEL_PRODUCT.format(positionOnWidgetHome, headerName),
            products
    )

    fun getMixTopViewIris(products: List<Product>, headerName: String, channelId: String, positionOnWidgetHome: String) = getBasicProductChannelView(
            Event.PRODUCT_VIEW_IRIS,
            Category.HOMEPAGE,
            CustomAction.IMPRESSION_ON_CAROUSEL_PRODUCT,
            Label.NONE,
            CustomActionField.LIST_CAROUSEL_PRODUCT.format(positionOnWidgetHome, headerName),
            products,
            channelId
    )


    fun getMixTopClick(products: List<Product>, headerName: String, channelId: String, positionOnWidgetHome: String, campaignCode: String) = getBasicProductChannelClick(
            Event.PRODUCT_CLICK,
            Category.HOMEPAGE,
            CustomAction.CLICK_ON_CAROUSEL_PRODUCT,
            channelId + " - " + headerName,
            CustomActionField.LIST_CAROUSEL_PRODUCT.format(positionOnWidgetHome, headerName),
            channelId,
            campaignCode,
            products
    )

    fun getMixTopSeeAllClick(channelId: String, headerName: String) = DataLayer.mapOf(
            Event.KEY, Event.CLICK_HOMEPAGE,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CustomAction.CLICK_VIEW_ALL_CAROUSEL,
            Label.KEY, channelId + " - " + headerName
    )

    fun getMixTopSeeAllCardClick(channelId: String, headerName: String, userId: String) = DataLayer.mapOf(
            Event.KEY, Event.CLICK_HOMEPAGE,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CustomAction.CLICK_VIEW_ALL_CAROUSEL_CARD,
            Label.KEY, channelId + " - " + headerName,
            Screen.KEY, Screen.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            Screen.KEY, Screen.DEFAULT,
            UserId.KEY, userId,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            ChannelId.KEY, channelId
    )

    fun getMixTopButtonClick(channelId: String,headerName: String, buttonName: String) = DataLayer.mapOf(
            Event.KEY, Event.CLICK_HOMEPAGE,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CustomAction.CLICK_BUTTON_CAROUSEL.format(buttonName),
            Label.KEY, channelId + " - " + headerName
    )

    fun mapGridToProductTracker(grid: DynamicHomeChannel.Grid, channelId: String, position: Int, persoType: String, categoryId: String) = Product(
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
        mapGridToProductTrackerComponent(it.value, channels.id, it.index, channels.trackingAttributionModel.persoType, channels.trackingAttributionModel.categoryId)
    }

    fun mapGridToProductTrackerComponent(grid: ChannelGrid, channelId: String, position: Int, persoType: String, categoryId: String) = Product(
            id = grid.id,
            name = grid.name,
            brand = "",
            category = "",
            channelId = channelId,
            isFreeOngkir = grid.isFreeOngkirActive,
            productPosition = position.toString(),
            productPrice = convertRupiahToInt(grid.price).toString(),
            variant = "",
            persoType = persoType,
            categoryId = categoryId,
            isTopAds = grid.isTopads
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

}