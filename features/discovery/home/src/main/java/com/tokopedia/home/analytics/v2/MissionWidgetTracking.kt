package com.tokopedia.home.analytics.v2

import android.annotation.SuppressLint
import android.os.Bundle
import com.tokopedia.home.analytics.v2.MissionWidgetTracking.CustomAction.Companion.DEFAULT_ID
import com.tokopedia.home.analytics.v2.MissionWidgetTracking.CustomAction.Companion.DEFAULT_VALUE
import com.tokopedia.home.analytics.v2.MissionWidgetTracking.CustomAction.Companion.DIMENSION_40
import com.tokopedia.home.analytics.v2.MissionWidgetTracking.CustomAction.Companion.DIMENSION_84
import com.tokopedia.home.analytics.v2.MissionWidgetTracking.CustomAction.Companion.DIMENSION_96
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMissionWidgetDataModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * Created by dhaba
 */
@SuppressLint("PII Data Exposure")
object MissionWidgetTracking : BaseTrackerConst() {
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

    fun sendMissionWidgetClicked(
        element: CarouselMissionWidgetDataModel,
        horizontalPosition: Int,
        userId: String
    ) {
        val bundle = Bundle()
        val creativeName = element.channelName
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(Action.KEY, CustomAction.EVENT_ACTION_CLICK)
        bundle.putString(Category.KEY, Category.HOMEPAGE)
        val eventLabel = CustomAction.EVENT_LABEL_FORMAT.format(
            element.channelId,
            element.headerName
        )
        bundle.putString(Label.KEY, eventLabel)
        bundle.putString(CustomAction.TRACKER_ID, CustomAction.TRACKER_ID_CLICKED)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CampaignCode.KEY, element.data.campaignCode)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(UserId.KEY, userId)
        bundle.putString(ChannelId.KEY, element.channelId)
        val promotion = Bundle()
        promotion.putString(Promotion.CREATIVE_NAME, creativeName)
        promotion.putString(Promotion.CREATIVE_SLOT, (horizontalPosition + 1).toString())
        promotion.putString(
            Promotion.ITEM_ID,
            CustomAction.ITEM_ID_FORMAT.format(
                element.data.id,
                DEFAULT_ID,
                element.data.shopId,
                DEFAULT_ID,
                DEFAULT_VALUE
            )
        )
        promotion.putString(
            Promotion.ITEM_NAME,
            CustomAction.ITEM_NAME_FORMAT.format(
                element.verticalPosition,
                CustomAction.DYNAMIC_CHANNEL_MISSION_WIDGET,
                CustomAction.BANNER,
                element.data.categoryID,
                element.data.recommendationType,
                element.data.title
            )
        )
        bundle.putParcelableArrayList(Promotion.KEY, arrayListOf(promotion))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            com.tokopedia.analytic_constant.Event.SELECT_CONTENT,
            bundle
        )
    }

    fun getMissionWidgetView(
        element: CarouselMissionWidgetDataModel,
        horizontalPosition: Int,
        userId: String
    ): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        val creativeName = element.channelName
        val creativeSlot = (horizontalPosition + 1).toString()
        val itemId = CustomAction.ITEM_ID_FORMAT.format(
            element.data.id,
            DEFAULT_ID,
            element.data.shopId,
            DEFAULT_ID,
            DEFAULT_VALUE
        )
        val itemName = CustomAction.ITEM_NAME_FORMAT.format(
            element.verticalPosition,
            CustomAction.DYNAMIC_CHANNEL_MISSION_WIDGET,
            CustomAction.BANNER,
            element.data.categoryID,
            element.data.recommendationType,
            element.data.title
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
            element.channelId,
            element.headerName
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
            .appendChannelId(element.channelId)
            .build()
    }

    fun sendMissionWidgetClickedToPdp(
        element: CarouselMissionWidgetDataModel,
        horizontalPosition: Int,
        userId: String
    ) {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(Action.KEY, CustomAction.EVENT_ACTION_CLICK_PRODUCT)
        bundle.putString(Category.KEY, Category.HOMEPAGE)
        val eventLabel = CustomAction.EVENT_LABEL_FORMAT.format(
            element.channelId,
            element.headerName
        )
        bundle.putString(Label.KEY, eventLabel)
        bundle.putString(CustomAction.TRACKER_ID, CustomAction.TRACKER_ID_CLICKED_PDP)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(UserId.KEY, userId)
        val item = Bundle()
        item.putString(Items.INDEX, (horizontalPosition + 1).toString())
        item.putString(Items.ITEM_BRAND, DEFAULT_VALUE)
        item.putString(Items.ITEM_CATEGORY, element.data.categoryID)
        item.putString(Items.ITEM_ID, element.data.productID)
        item.putString(Items.ITEM_NAME, element.data.productName)
        item.putString(Items.ITEM_VARIANT, DEFAULT_VALUE)
        item.putFloat(Items.PRICE, CustomAction.DEFAULT_PRICE)
        val itemList = CustomAction.ITEM_LIST_FORMAT.format(
            element.verticalPosition,
            if (element.data.isTopads) CustomAction.TOPADS else CustomAction.NON_TOPADS,
            if (element.data.isCarousel) CustomAction.CAROUSEL else CustomAction.NON_CAROUSEL,
            element.data.recommendationType,
            element.data.pageName,
            element.data.buType,
            element.data.title
        )
        item.putString(DIMENSION_40, itemList)
        item.putString(DIMENSION_84, element.data.id.toString())
        item.putString(DIMENSION_96, CustomAction.DIMENSION_96_FORMAT.format(DEFAULT_ID, DEFAULT_VALUE))
        bundle.putParcelableArrayList(Items.KEY, arrayListOf(item))
        bundle.putString(ItemList.KEY, itemList)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            com.tokopedia.analytic_constant.Event.SELECT_CONTENT,
            bundle
        )
    }

    fun getMissionWidgetProductView(
        element: CarouselMissionWidgetDataModel,
        horizontalPosition: Int,
        userId: String
    ): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        val listProducts = arrayListOf(
            Product(
                brand = DEFAULT_VALUE,
                category = element.data.categoryID,
                channelId = element.data.id.toString(),
                persoType = DEFAULT_ID,
                categoryId = DEFAULT_VALUE,
                id = element.data.productID,
                name = element.data.productName,
                productPosition = (horizontalPosition + 1).toString(),
                productPrice = CustomAction.DEFAULT_PRICE.toString(),
                variant = DEFAULT_VALUE,
                isFreeOngkir = false
            )
        )

        val itemList = CustomAction.ITEM_LIST_FORMAT.format(
            element.verticalPosition,
            if (element.data.isTopads) CustomAction.TOPADS else CustomAction.NON_TOPADS,
            if (element.data.isCarousel) CustomAction.CAROUSEL else CustomAction.NON_CAROUSEL,
            element.data.recommendationType,
            element.data.pageName,
            element.data.buType,
            element.data.title
        )
        val eventLabel = CustomAction.EVENT_LABEL_FORMAT.format(
            element.channelId,
            element.headerName
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
}
