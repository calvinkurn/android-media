package com.tokopedia.home.analytics.v2

import android.os.Bundle
import com.tokopedia.home.analytics.v2.Mission4SquareWidgetTracker.CustomAction.Companion.DEFAULT_ID
import com.tokopedia.home.analytics.v2.Mission4SquareWidgetTracker.CustomAction.Companion.DEFAULT_VALUE
import com.tokopedia.home.analytics.v2.Mission4SquareWidgetTracker.CustomAction.Companion.DIMENSION_40
import com.tokopedia.home.analytics.v2.Mission4SquareWidgetTracker.CustomAction.Companion.DIMENSION_84
import com.tokopedia.home.analytics.v2.Mission4SquareWidgetTracker.CustomAction.Companion.DIMENSION_96
import com.tokopedia.home_component.visitable.Mission4SquareUiModel
import com.tokopedia.home_component.visitable.shorten.ItemMissionWidgetUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import java.util.*

object Mission4SquareWidgetTracker : BaseTrackerConst() {


    fun sendMissionWidgetClicked(element: ItemMissionWidgetUiModel, horizontalPosition: Int, userId: String) {
        sendMissionWidgetClicked(element.tracker.channelId, userId, horizontalPosition, element.tracker.channelId, element.tracker.headerName, element.tracker.campaignCode, element.tracker.gridId, element.shopId, horizontalPosition, element.categoryId, element.recommendationType, element.card.title().first)
    }

    fun sendMissionWidgetClicked(
        element: Mission4SquareUiModel,
        horizontalPosition: Int,
        userId: String
    ) {
        sendMissionWidgetClicked(element.channelName, userId, horizontalPosition, element.channelId, element.header.name, element.data.campaignCode, element.data.id.toString().orEmpty(), element.data.shopId, element.verticalPosition, element.data.categoryID, element.data.recommendationType, element.data.title)
    }

    private fun sendMissionWidgetClicked(channelName: String, userId: String, horizontalPosition: Int,
                                         channelId: String, headerName: String, campaignCode: String, gridId: String, shopId: String, verticalPosition: Int, categoryID: String, recommendationType: String, title: String) {
        val bundle = Bundle()
        val creativeName = channelName
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(Action.KEY, CustomAction.EVENT_ACTION_CLICK)
        bundle.putString(Category.KEY, Category.HOMEPAGE)
        val eventLabel = CustomAction.EVENT_LABEL_FORMAT.format(
            channelId,
            headerName
        )
        bundle.putString(Label.KEY, eventLabel)
        bundle.putString(CustomAction.TRACKER_ID, CustomAction.TRACKER_ID_CLICKED)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CampaignCode.KEY, campaignCode)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(UserId.KEY, userId)
        bundle.putString(ChannelId.KEY, channelId)
        val promotion = Bundle()
        promotion.putString(Promotion.CREATIVE_NAME, creativeName)
        promotion.putString(Promotion.CREATIVE_SLOT, (horizontalPosition + 1).toString())
        promotion.putString(
            Promotion.ITEM_ID,
            CustomAction.ITEM_ID_FORMAT.format(
                gridId,
                DEFAULT_ID,
                shopId,
                DEFAULT_ID,
                DEFAULT_VALUE
            )
        )
        promotion.putString(
            Promotion.ITEM_NAME,
            CustomAction.ITEM_NAME_FORMAT.format(
                verticalPosition,
                CustomAction.DYNAMIC_CHANNEL_MISSION_WIDGET,
                CustomAction.BANNER,
                categoryID,
                recommendationType,
                title
            )
        )
        bundle.putParcelableArrayList(Promotion.KEY, arrayListOf(promotion))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            com.tokopedia.analytic_constant.Event.SELECT_CONTENT,
            bundle
        )
    }

    fun getMissionWidgetView(
        element: ItemMissionWidgetUiModel,
        horizontalPosition: Int,
        userId: String
    ): Map<String, Any> {
        return getMissionWidgetView(horizontalPosition, userId, element.tracker.channelName, element.tracker.gridId,
            element.shopId, element.verticalPosition, element.categoryId, element.recommendationType,
            element.card.title().first, element.tracker.channelId, element.tracker.headerName)
    }

    fun getMissionWidgetView(
        element: Mission4SquareUiModel,
        horizontalPosition: Int,
        userId: String
    ): Map<String, Any> {
        return getMissionWidgetView(horizontalPosition, userId, element.channelName, element.data.id.toString(),
            element.data.shopId, element.verticalPosition, element.data.categoryID, element.data.recommendationType,
            element.data.title, element.channelId, element.header.name)
    }

    private fun getMissionWidgetView(horizontalPosition: Int, userId: String, channelName: String,
                                     gridId: String, shopId: String, verticalPosition: Int,
                                     categoryID: String, recommendationType: String, title: String,
                                     channelId: String, headerName: String): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        val creativeName = channelName
        val creativeSlot = (horizontalPosition + 1).toString()
        val itemId = CustomAction.ITEM_ID_FORMAT.format(
                gridId,
                DEFAULT_ID,
                shopId,
                DEFAULT_ID,
                DEFAULT_VALUE
        )
        val itemName = CustomAction.ITEM_NAME_FORMAT.format(
                verticalPosition,
                CustomAction.DYNAMIC_CHANNEL_MISSION_WIDGET,
                CustomAction.BANNER,
                categoryID,
                recommendationType,
                title
        )
        val listPromotions = arrayListOf(
                Promotion(
                    creative = creativeName,
                    position = creativeSlot,
                    id = itemId,
                    name = itemName
                )
        )
        val eventLabel = CustomAction.EVENT_LABEL_FORMAT.format(
                channelId,
                headerName
        )
        return trackingBuilder.constructBasicPromotionView(
                event = Event.PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = CustomAction.EVENT_ACTION_IMPRESSION_BANNER,
                eventLabel = eventLabel,
                promotions = listPromotions
        )
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .appendCustomKeyValue(CustomAction.TRACKER_ID, CustomAction.TRACKER_ID_IMPRESSION)
                .appendChannelId(channelId)
                .build()
    }

    fun sendMissionWidgetClickedToPdp(
        element: ItemMissionWidgetUiModel,
        horizontalPosition: Int,
        userId: String
    ) {
        sendMissionWidgetClickedToPdp(userId, horizontalPosition, element.tracker.channelId, element.tracker.headerName,
            element.categoryId, element.tracker.productId, element.productName, element.verticalPosition,
            element.isTopAds, element.isCarousel, element.recommendationType,element.pageName,
            element.buType, element.card.title().first, element.tracker.gridId)
    }

    fun sendMissionWidgetClickedToPdp(
        element: Mission4SquareUiModel,
        horizontalPosition: Int,
        userId: String
    ) {
        sendMissionWidgetClickedToPdp(userId, horizontalPosition, element.channelId, element.header.name,
            element.data.categoryID, element.data.productID, element.data.productName, element.verticalPosition,
            element.data.isTopads, element.data.isCarousel, element.data.recommendationType,element.data.pageName,
            element.data.buType, element.data.title, element.data.id.toString())
    }

    private fun sendMissionWidgetClickedToPdp(userId: String, horizontalPosition: Int, channelId: String, headerName: String,
                                              categoryID: String, productID : String, productName : String, verticalPosition: Int,
                                              isTopads:Boolean, isCarousel : Boolean, recommendationType: String,
                                              pageName : String, buType : String, title : String, gridId: String) {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(Action.KEY, CustomAction.EVENT_ACTION_CLICK_PRODUCT)
        bundle.putString(Category.KEY, Category.HOMEPAGE)
        val eventLabel = CustomAction.EVENT_LABEL_FORMAT.format(
                channelId,
                headerName
        )
        bundle.putString(Label.KEY, eventLabel)
        bundle.putString(CustomAction.TRACKER_ID, CustomAction.TRACKER_ID_CLICKED_PDP)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(UserId.KEY, userId)
        val item = Bundle()
        item.putString(Items.INDEX, (horizontalPosition + 1).toString())
        item.putString(Items.ITEM_BRAND, DEFAULT_VALUE)
        item.putString(Items.ITEM_CATEGORY, categoryID)
        item.putString(Items.ITEM_ID, productID)
        item.putString(Items.ITEM_NAME, productName)
        item.putString(Items.ITEM_VARIANT, DEFAULT_VALUE)
        item.putFloat(Items.PRICE, CustomAction.DEFAULT_PRICE)
        val itemList = CustomAction.ITEM_LIST_FORMAT.format(
                verticalPosition,
                if (isTopads) CustomAction.TOPADS else CustomAction.NON_TOPADS,
                if (isCarousel) CustomAction.CAROUSEL else CustomAction.NON_CAROUSEL,
                recommendationType,
                pageName,
                buType,
                title
        )
        item.putString(DIMENSION_40, itemList)
        item.putString(DIMENSION_84, gridId)
        item.putString(DIMENSION_96, CustomAction.DIMENSION_96_FORMAT.format(DEFAULT_ID, DEFAULT_VALUE))
        bundle.putParcelableArrayList(Items.KEY, arrayListOf(item))
        bundle.putString(ItemList.KEY, itemList)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                com.tokopedia.analytic_constant.Event.SELECT_CONTENT,
                bundle
        )
    }

    fun getMissionWidgetProductView(
        element: ItemMissionWidgetUiModel,
        horizontalPosition: Int,
        userId: String
    ): Map<String, Any> {
        return getMissionWidgetProductView(horizontalPosition, userId, element.categoryId, element.tracker.gridId,
            element.tracker.productId, element.productName, element.verticalPosition, element.isTopAds,
            element.isCarousel, element.recommendationType, element.pageName, element.buType,
            element.card.title().first, element.tracker.channelId, element.tracker.headerName)
    }

    fun getMissionWidgetProductView(
        element: Mission4SquareUiModel,
        horizontalPosition: Int,
        userId: String
    ): Map<String, Any> {
        return getMissionWidgetProductView(horizontalPosition, userId, element.data.categoryID, element.data.id.toString(),
            element.data.productID, element.data.productName,element.verticalPosition, element.data.isTopads,
            element.data.isCarousel, element.data.recommendationType, element.data.pageName, element.data.buType,
            element.data.title, element.channelId, element.header.name)
    }

    private fun getMissionWidgetProductView(horizontalPosition: Int, userId: String, categoryID: String,
                                            gridId: String, productID: String, productName: String, verticalPosition: Int,
                                            isTopads: Boolean, isCarousel: Boolean, recommendationType: String,
                                            pageName: String, buType: String, title : String, channelId: String,
                                            headerName: String): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        val listProducts = arrayListOf(
                Product(
                    brand = DEFAULT_VALUE,
                    category = categoryID,
                    channelId = gridId,
                    persoType = DEFAULT_ID,
                    categoryId = DEFAULT_VALUE,
                    id = productID,
                    name = productName,
                    productPosition = (horizontalPosition + 1).toString(),
                    productPrice = CustomAction.DEFAULT_PRICE.toString(),
                    variant = DEFAULT_VALUE,
                    isFreeOngkir = false
                )
        )

        val itemList = CustomAction.ITEM_LIST_FORMAT.format(
                verticalPosition,
                if (isTopads) CustomAction.TOPADS else CustomAction.NON_TOPADS,
                if (isCarousel) CustomAction.CAROUSEL else CustomAction.NON_CAROUSEL,
                recommendationType,
                pageName,
                buType,
                title
        )
        val eventLabel = CustomAction.EVENT_LABEL_FORMAT.format(
                channelId,
                headerName
        )
        return trackingBuilder.constructBasicProductView(
                event = Event.PRODUCT_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = CustomAction.EVENT_ACTION_IMPRESSION_PRODUCT,
                eventLabel = eventLabel,
                products = listProducts,
                list = itemList
        )
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .appendCustomKeyValue(CustomAction.TRACKER_ID, CustomAction.TRACKER_ID_PRODUCT)
                .build()
    }

    private class CustomAction {
        companion object {
            const val EVENT_ACTION_CLICK = "click on banner dynamic channel mission widget"
            const val EVENT_ACTION_CLICK_PRODUCT = "click on product dynamic channel mission widget"
            const val EVENT_ACTION_IMPRESSION_BANNER =
                "impression on banner dynamic channel mission widget"
            const val EVENT_ACTION_IMPRESSION_PRODUCT =
                "impression on product dynamic channel mission widget"
            const val EVENT_LABEL_FORMAT = "%s - %s"
            const val TRACKER_ID = "trackerId"
            const val TRACKER_ID_CLICKED = "40381"
            const val TRACKER_ID_CLICKED_PDP = "40380"
            const val TRACKER_ID_IMPRESSION = "40379"
            const val TRACKER_ID_PRODUCT = "40378"
            const val DEFAULT_VALUE = ""
            const val DEFAULT_PRICE = 0f
            const val DEFAULT_ID = "0"
            const val ITEM_ID_FORMAT = "%s_%s_%s_%s_%s"
            const val DYNAMIC_CHANNEL_MISSION_WIDGET = "dynamic channel mission widget"
            const val BANNER = "banner"
            const val ITEM_NAME_FORMAT = "/ - p%s - %s - %s - %s - %s - %s"
            const val ITEM_LIST_FORMAT =
                "/ - p%s - $DYNAMIC_CHANNEL_MISSION_WIDGET - product - %s - %s - %s - %s - %s - %s"
            const val TOPADS = "topads"
            const val NON_TOPADS = "non topads"
            const val CAROUSEL = "carousel"
            const val NON_CAROUSEL = "non carousel"
            const val DIMENSION_40 = "dimension40"
            const val DIMENSION_84 = "dimension84"
            const val DIMENSION_96 = "dimension96"
            const val DIMENSION_96_FORMAT = "%s_%s"
        }
    }
}

