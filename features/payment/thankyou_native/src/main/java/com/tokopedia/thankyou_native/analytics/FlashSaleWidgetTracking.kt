package com.tokopedia.thankyou_native.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.discovery.common.constants.SearchConstant.ProductCardLabel.LABEL_FULFILLMENT
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.LabelGroup
import com.tokopedia.home_component.util.getTopadsString
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

class FlashSaleWidgetTracking: BaseTrackerConst() {

    companion object {
        private const val EVENT_CATEGORY_ORDER_COMPLETE = "order complete"
        private const val EVENT_ACTION_CLICK_FLASH_SALE_PRODUCT = "click on product dynamic channel thank you page"
        private const val EVENT_ACTION_IMPRESSION_FLASH_SALE_PRODUCT = "impression on product dynamic channel thank you page"
        private const val EVENT_ACTION_CLICK_VIEW_ALL_PRODUCT = "click view all on dynamic channel thank you page"
        private const val PRODUCT_LIST = "/ - p%s - %s - %s - carousel - %s - %s - %s - %s"

        private const val VIEW_ITEM_LIST = "view_item_list"
        private const val CLICK_PAYMENT = "clickPayment"
    }

    private val LIST_CAROUSEL_PRODUCT = PRODUCT_LIST.format("%s", "dynamic channel thank you page - product", "%s", "%s", "%s", "%s", "%s")

    fun getMixLeftProductView(channel: ChannelModel, grid: ChannelGrid, position: Int, positionOnTyp: Int, userId: String): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        return trackingBuilder.constructBasicProductView(
            event = VIEW_ITEM_LIST,
            eventCategory = EVENT_CATEGORY_ORDER_COMPLETE,
            eventAction = EVENT_ACTION_IMPRESSION_FLASH_SALE_PRODUCT,
            eventLabel = "",
            products = listOf(
                Product(
                    name = grid.name,
                    id = grid.id,
                    productPrice = convertRupiahToInt(
                        grid.price
                    ).toString(),
                    brand = Value.NONE_OTHER,
                    category = grid.categoryBreadcrumbs,
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
                    isCarousel = true,
                    warehouseId = grid.warehouseId,
                    isFulfillment = grid.labelGroup.hasLabelGroupFulfillment()
                )
            ),
            list = LIST_CAROUSEL_PRODUCT.format(
                positionOnTyp,
                grid.getTopadsString(),
                grid.recommendationType,
                channel.pageName,
                channel.trackingAttributionModel.galaxyAttribution,
                channel.channelHeader.name
            )
        )
            .appendChannelId(channel.id)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendUserId(userId)
            .build()
    }

    private fun getProductClick(channel: ChannelModel, grid: ChannelGrid, position: Int, positionOnTyp: Int, userId: String):  Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        return trackingBuilder.constructBasicProductClick(
            event = Event.SELECT_CONTENT,
            eventCategory = EVENT_CATEGORY_ORDER_COMPLETE,
            eventAction = EVENT_ACTION_CLICK_FLASH_SALE_PRODUCT,
            eventLabel = channel.id + " - " + channel.channelHeader.name,
            products = listOf(
                Product(
                    name = grid.name,
                    id = grid.id,
                    productPrice = convertRupiahToInt(
                        grid.price
                    ).toString(),
                    brand = Value.NONE_OTHER,
                    category = grid.categoryBreadcrumbs,
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
                    isCarousel = true,
                    warehouseId = grid.warehouseId,
                    isFulfillment = grid.labelGroup.hasLabelGroupFulfillment()
                )
            ),
            list = LIST_CAROUSEL_PRODUCT.format(
                positionOnTyp,
                grid.getTopadsString(),
                grid.recommendationType,
                channel.pageName,
                channel.trackingAttributionModel.galaxyAttribution,
                channel.channelHeader.name
            )
        )
            .appendChannelId(channel.id)
            .appendScreen(Screen.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCampaignCode(channel.trackingAttributionModel.campaignCode)
            .appendUserId(userId)
            .build()
    }

    fun sendProductClick(channel: ChannelModel, grid: ChannelGrid, position: Int, positionOnTyp: Int, userId: String) {
        getTracker().sendEnhanceEcommerceEvent(getProductClick(channel, grid, position, positionOnTyp, userId))
    }

    private fun getFlashSaleWidgetLoadMoreCard(channel: ChannelModel, userId: String): HashMap<String, Any> {
        return DataLayer.mapOf(
            Event.KEY, CLICK_PAYMENT,
            Category.KEY, EVENT_CATEGORY_ORDER_COMPLETE,
            Action.KEY, EVENT_ACTION_CLICK_VIEW_ALL_PRODUCT,
            Label.KEY, channel.id + " - " + channel.channelHeader.name,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            ChannelId.KEY, channel.id,
            Screen.KEY, Screen.DEFAULT,
            UserId.KEY, userId,
            BusinessUnit.KEY, BusinessUnit.DEFAULT
        ) as HashMap<String, Any>
    }

    fun sendFlashSaleWidgetLoadMoreCard(channel: ChannelModel, userId: String) {
        getTracker().sendEnhanceEcommerceEvent(getFlashSaleWidgetLoadMoreCard(channel, userId))
    }
}

private fun List<LabelGroup>.hasLabelGroupFulfillment(): Boolean{
    return this.any { it.position == LABEL_FULFILLMENT }
}
