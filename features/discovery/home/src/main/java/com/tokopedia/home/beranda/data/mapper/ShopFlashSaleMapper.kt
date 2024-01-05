package com.tokopedia.home.beranda.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper
import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper.mapToChannelGrid
import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper.mapToHomeComponentHeader
import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper.mapToTrackingAttributionModel
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.model.ChannelViewAllCard
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselProductCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselViewAllCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.util.ChannelStyleUtil.isHideTimer
import com.tokopedia.home_component.widget.shop_flash_sale.ShopFlashSaleTimerDataModel
import com.tokopedia.home_component.widget.shop_flash_sale.ShopFlashSaleWidgetDataModel
import com.tokopedia.home_component.widget.shop_flash_sale.item.ShopFlashSaleErrorDataModel
import com.tokopedia.home_component.widget.shop_flash_sale.item.ShopFlashSaleProductGridShimmerDataModel
import com.tokopedia.home_component.widget.shop_flash_sale.tab.ShopFlashSaleTabDataModel
import com.tokopedia.home_component.widget.shop_tab.ShopTabDataModel
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.unifycomponents.CardUnify2

object ShopFlashSaleMapper {

    private const val VIEW_ALL_CARD_TITLE_BELOW_THRESHOLD = "Ada produk menarik lain di toko ini!"
    private const val VIEW_ALL_CARD_TITLE_ABOVE_THRESHOLD = "Masih ada promo lainnya di toko ini!"

    /**
     *  Map Dynamic Channel data to Shop Flash Sale model
     */
    fun mapShopFlashSaleWidgetDataModel(
        channel: DynamicHomeChannel.Channels,
        verticalPosition: Int,
    ): ShopFlashSaleWidgetDataModel {
        val timer = if(channel.styleParam.isHideTimer()) {
            null
        } else {
            ShopFlashSaleTimerDataModel(isLoading = true)
        }
        return ShopFlashSaleWidgetDataModel(
            id = channel.id,
            channelHeader = channel.header.mapToHomeComponentHeader(),
            channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(
                channel,
                verticalPosition,
                mapGrids = false
            ),
            tabList = channel.grids.mapIndexed { index, grid ->
                mapShopFlashSaleTabModel(channel, verticalPosition, grid, index)
            },
            timer = timer,
            itemList = ShopFlashSaleProductGridShimmerDataModel.getAsList(),
        )
    }

    private fun mapShopFlashSaleTabModel(
        channel: DynamicHomeChannel.Channels,
        verticalPosition: Int,
        grid: DynamicHomeChannel.Grid,
        index: Int
    ): ShopFlashSaleTabDataModel {
        val isActivated = index == 0
        return ShopFlashSaleTabDataModel(
            grid.mapToChannelGrid(index, useDtAsShopBadge = true),
            channel.mapToTrackingAttributionModel(verticalPosition),
            isActivated,
            mapShopTabModel(grid, isActivated)
        )
    }

    private fun mapShopTabModel(grid: DynamicHomeChannel.Grid, isActivated: Boolean): ShopTabDataModel {
        return ShopTabDataModel(
            id = grid.id,
            shopName = grid.name,
            imageUrl = grid.imageUrl,
            badgesUrl = grid.badges.getOrNull(0)?.imageUrl.orEmpty(),
            isActivated = isActivated
        )
    }

    /**
     * Inserting items list and timer into Shop Flash Sale model.
     */
    fun mapShopFlashSaleItemList(
        currentDataModel: ShopFlashSaleWidgetDataModel,
        recomData: List<RecommendationWidget>,
    ): ShopFlashSaleWidgetDataModel {
        return if (recomData.isNotEmpty() && recomData.first().recommendationItemList.isNotEmpty()) {
            val recomWidget = recomData.first()
            val trackingModel = currentDataModel.channelModel.trackingAttributionModel
            val carouselList = mutableListOf<Visitable<CommonCarouselProductCardTypeFactory>>().apply {
                addAll(getProducts(recomWidget, trackingModel))
                add(getViewAllCard(trackingModel, recomWidget.seeMoreAppLink, currentDataModel))
            }
            val timer = if(currentDataModel.channelModel.channelConfig.styleParam.isHideTimer()) {
                null
            } else {
                ShopFlashSaleTimerDataModel(recomWidget.endDate, isLoading = false)
            }
            currentDataModel.copy(
                itemList = carouselList,
                timer = timer
            )
        } else {
            getLoadingShopFlashSale(currentDataModel)
        }
    }

    fun getLoadingShopFlashSale(currentDataModel: ShopFlashSaleWidgetDataModel): ShopFlashSaleWidgetDataModel {
        return currentDataModel.copy(
            itemList = ShopFlashSaleProductGridShimmerDataModel.getAsList(),
            timer = currentDataModel.timer?.copy(isLoading = true)
        )
    }

    fun getErrorShopFlashSale(currentDataModel: ShopFlashSaleWidgetDataModel): ShopFlashSaleWidgetDataModel {
        return currentDataModel.copy(
            itemList = ShopFlashSaleErrorDataModel.getAsList(),
            timer = null
        )
    }

    private fun getProducts(
        recomWidget: RecommendationWidget,
        trackingModel: TrackingAttributionModel,
    ): List<CarouselProductCardDataModel> {
        return recomWidget.recommendationItemList.mapIndexed { index, item ->
            CarouselProductCardDataModel(
                productModel = item.toProductCardModel(
                    cardType = CardUnify2.TYPE_BORDER,
                ),
                grid = item.mapToChannelGrid(index),
                trackingAttributionModel = trackingModel,
                applink = item.appUrl,
            )
        }
    }

    private fun getViewAllCard(
        trackingModel: TrackingAttributionModel,
        seeMoreAppLink: String,
        currentDataModel: ShopFlashSaleWidgetDataModel,
    ): CarouselViewAllCardDataModel {
        val viewAllCardTitle = if(seeMoreAppLink.isEmpty())
            VIEW_ALL_CARD_TITLE_BELOW_THRESHOLD
        else VIEW_ALL_CARD_TITLE_ABOVE_THRESHOLD
        return CarouselViewAllCardDataModel(
            trackingAttributionModel = trackingModel,
            applink = currentDataModel.tabList.firstOrNull { it.isActivated }?.channelGrid?.applink.orEmpty(),
            channelViewAllCard = ChannelViewAllCard(
                title = viewAllCardTitle,
            ),
            animateOnPress = CardUnify2.ANIMATE_OVERLAY_BOUNCE,
        )
    }
}
