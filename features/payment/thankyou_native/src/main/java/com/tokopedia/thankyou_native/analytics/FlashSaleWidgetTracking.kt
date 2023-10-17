package com.tokopedia.thankyou_native.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.discovery.common.constants.SearchConstant.ProductCardLabel.LABEL_FULFILLMENT
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.LabelGroup
import com.tokopedia.home_component.util.getTopadsString
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.PRODUCT_VIEW

class FlashSaleWidgetTracking: BaseTrackerConst() {

    companion object {
        private const val EVENT_CATEGORY_ORDER_COMPLETE = "order complete"
        private const val EVENT_ACTION_CLICK_FLASH_SALE_PRODUCT = "click on product dynamic channel thank you page"
        private const val EVENT_ACTION_IMPRESSION_FLASH_SALE_PRODUCT = "impression on product dynamic channel thank you page"
        private const val EVENT_ACTION_CLICK_VIEW_ALL_PRODUCT = "click view all on dynamic channel thank you page"
        private const val PRODUCT_LIST = "/ - p%s - %s - %s - carousel - %s - %s - %s - %s"

        private const val VIEW_ITEM_LIST = "view_item_list"
        private const val CLICK_PAYMENT = "clickPayment"
        private const val PAYMENT = "payment"
        private const val ANDROID_TRACKER_ID_VIEW = "47651"
        private const val ANDROID_TRACKER_ID_CLICK = "47652"
        private const val ANDROID_TRACKER_ID_VIEW_ALL_CLICK = "47710"
        private const val MERCHANT_CODE = "merchantCode"
        private const val PAYMENT_ID = "paymentId"
        private const val PAYMENT_METHOD = "paymentMethod"
        private const val ANDROID = "android"
        private const val ENVIRONMENT = "environment"
        private const val SCROOGE_ID = "scroogeId"
    }

    private val LIST_CAROUSEL_PRODUCT = PRODUCT_LIST.format("%s", "dynamic channel thank you page - product", "%s", "%s", "%s", "%s", "%s")

    fun getMixLeftProductView(
        channel: ChannelModel,
        grid: ChannelGrid,
        position: Int,
        positionOnTyp: Int,
        userId: String,
        merchantCode: String,
        paymentId: String,
        paymentMethod: String,
    ): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        return trackingBuilder.constructBasicProductView(
            event = PRODUCT_VIEW,
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
            .appendBusinessUnit(PAYMENT)
            .appendUserId(userId)
            .appendCustomKeyValue(ItemList.KEY, LIST_CAROUSEL_PRODUCT.format(
                positionOnTyp,
                grid.getTopadsString(),
                grid.recommendationType,
                channel.pageName,
                channel.trackingAttributionModel.galaxyAttribution,
                channel.channelHeader.name
            ))
            .appendCustomKeyValue(TrackerId.KEY, ANDROID_TRACKER_ID_VIEW)
            .appendCustomKeyValue(ENVIRONMENT, ANDROID)
            .appendCustomKeyValue(MERCHANT_CODE, merchantCode)
            .appendCustomKeyValue(PAYMENT_ID, paymentId)
            .appendCustomKeyValue(PAYMENT_METHOD, paymentMethod)
            .appendCustomKeyValue(SCROOGE_ID, String.EMPTY)
            .build()
    }

    private fun getProductClick(
        channel: ChannelModel,
        grid: ChannelGrid,
        position: Int,
        positionOnTyp: Int,
        userId: String,
        merchantCode: String,
        paymentId: String,
        paymentMethod: String,
    ):  Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        return trackingBuilder.constructBasicProductClick(
            event = Event.PRODUCT_CLICK,
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
            .appendBusinessUnit(PAYMENT)
            .appendCampaignCode(channel.trackingAttributionModel.campaignCode)
            .appendUserId(userId)
            .appendCustomKeyValue(ItemList.KEY, LIST_CAROUSEL_PRODUCT.format(
                positionOnTyp,
                grid.getTopadsString(),
                grid.recommendationType,
                channel.pageName,
                channel.trackingAttributionModel.galaxyAttribution,
                channel.channelHeader.name
            ))
            .appendCustomKeyValue(TrackerId.KEY, ANDROID_TRACKER_ID_CLICK)
            .appendCustomKeyValue(ENVIRONMENT, ANDROID)
            .appendCustomKeyValue(MERCHANT_CODE, merchantCode)
            .appendCustomKeyValue(PAYMENT_ID, paymentId)
            .appendCustomKeyValue(PAYMENT_METHOD, paymentMethod)
            .appendCustomKeyValue(SCROOGE_ID, String.EMPTY)
            .build()
    }

    fun sendProductClick(
        channel: ChannelModel,
        grid: ChannelGrid,
        position: Int, positionOnTyp: Int,
        userId: String,
        merchantCode: String,
        paymentId: String,
        paymentMethod: String,
    ) {
        getTracker().sendEnhanceEcommerceEvent(
            getProductClick(channel, grid, position, positionOnTyp, userId, merchantCode, paymentId, paymentMethod)
        )
    }

    private fun getFlashSaleWidgetLoadMoreCard(
        channel: ChannelModel,
        userId: String,
        merchantCode: String,
        paymentId: String,
        paymentMethod: String,
    ): HashMap<String, Any> {
        return DataLayer.mapOf(
            Event.KEY, CLICK_PAYMENT,
            Category.KEY, EVENT_CATEGORY_ORDER_COMPLETE,
            Action.KEY, EVENT_ACTION_CLICK_VIEW_ALL_PRODUCT,
            Label.KEY, channel.id + " - " + channel.channelHeader.name,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            ChannelId.KEY, channel.id,
            Screen.KEY, Screen.DEFAULT,
            UserId.KEY, userId,
            BusinessUnit.KEY, PAYMENT,
            TrackerId.KEY, ANDROID_TRACKER_ID_VIEW_ALL_CLICK,
            ENVIRONMENT, ANDROID,
            MERCHANT_CODE, merchantCode,
            PAYMENT_ID, paymentId,
            PAYMENT_METHOD, paymentMethod,
            SCROOGE_ID, String.EMPTY
        ) as HashMap<String, Any>
    }

    fun sendFlashSaleWidgetLoadMoreCard(
        channel: ChannelModel,
        userId: String,
        merchantCode: String,
        paymentId: String,
        paymentMethod: String,
    ) {
        getTracker().sendEnhanceEcommerceEvent(
            getFlashSaleWidgetLoadMoreCard(channel, userId, merchantCode, paymentId, paymentMethod)
        )
    }
}

private fun List<LabelGroup>.hasLabelGroupFulfillment(): Boolean{
    return this.any { it.position == LABEL_FULFILLMENT }
}
