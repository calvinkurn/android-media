package com.tokopedia.home.analytics.v2

import android.os.Bundle
import com.tokopedia.home.analytics.v2.MissionWidgetTracking.CustomAction.Companion.DEFAULT_BANNER_ID
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMissionWidgetDataModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * Created by dhaba
 */
object MissionWidgetTracking : BaseTrackerConst() {
    private class CustomAction {
        companion object {
            const val EVENT_ACTION_CLICK = "click on banner dynamic channel mission widget"
            const val EVENT_ACTION_CLICK_PRODUCT = "click on product dynamic channel mission widget"
            const val EVENT_ACTION_IMPRESSION = "impression on banner dynamic channel mission widget"
            const val EVENT_LABEL_FORMAT = "%s - %s"
            const val TRACKER_ID = "trackerId"
            const val TRACKER_ID_CLICKED = "32188"
            const val TRACKER_ID_CLICKED_PDP = "34629"
            const val TRACKER_ID_IMPRESSION = "32076"
            const val DEFAULT_VALUE = ""
            const val DEFAULT_PRICE = "0"
            const val DEFAULT_BANNER_ID = "0"
            const val ITEM_ID_FORMAT = "%s_%s_%s_%s"
            const val DYNAMIC_CHANNEL_MISSION_WIDGET = "dynamic channel mission widget"
            const val BANNER = "banner"
            const val ITEM_NAME_FORMAT = "/ - p%s - %s - %s - %s"
            const val ITEM_LIST_FORMAT = "/ - p%s - $DYNAMIC_CHANNEL_MISSION_WIDGET - product - %s - %s - %s - %s - %s - %s"
            const val TOPADS = "topads"
            const val NON_TOPADS = "non topads"
            const val CAROUSEL = "carousel"
            const val NON_CAROUSEL = "non carousel"
        }
    }

    fun sendMissionWidgetClicked(element: CarouselMissionWidgetDataModel, horizontalPosition: Int, userId: String) {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(Action.KEY, CustomAction.EVENT_ACTION_CLICK)
        bundle.putString(Category.KEY, Category.HOMEPAGE)
        bundle.putString(
            Label.KEY,
            CustomAction.EVENT_LABEL_FORMAT.format(
                element.id,
                element.title
            )
        )
        bundle.putString(CustomAction.TRACKER_ID, CustomAction.TRACKER_ID_CLICKED)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CampaignCode.KEY, element.channel.trackingAttributionModel.campaignCode)
        bundle.putString(Label.CHANNEL_LABEL, element.id.toString())
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(UserId.KEY, userId)
        val promotion = Bundle()
        promotion.putString(Promotion.CREATIVE_NAME, CustomAction.DEFAULT_VALUE)
        promotion.putString(Promotion.CREATIVE_SLOT, (horizontalPosition + 1).toString())
        promotion.putString(
            Promotion.ITEM_ID,
            CustomAction.ITEM_ID_FORMAT.format(
                element.id,
                DEFAULT_BANNER_ID,
                element.channel.trackingAttributionModel.persoType,
                element.channel.trackingAttributionModel.categoryId
            )
        )
        promotion.putString(
            Promotion.ITEM_NAME,
            CustomAction.ITEM_NAME_FORMAT.format(
                element.verticalPosition,
                CustomAction.DYNAMIC_CHANNEL_MISSION_WIDGET,
                CustomAction.BANNER,
                element.title
            )
        )
        bundle.putParcelableArrayList(Promotion.KEY, arrayListOf(promotion))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Ecommerce.PROMO_CLICK, bundle)
    }

    fun getMissionWidgetView(element: CarouselMissionWidgetDataModel, horizontalPosition: Int, userId: String) : Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        val creativeName = CustomAction.DEFAULT_VALUE
        val creativeSlot = (horizontalPosition + 1).toString()
        val itemId = CustomAction.ITEM_ID_FORMAT.format(
            element.id,
            DEFAULT_BANNER_ID,
            element.channel.trackingAttributionModel.persoType,
            element.channel.trackingAttributionModel.categoryId
        )
        val itemName = CustomAction.ITEM_NAME_FORMAT.format(
            element.verticalPosition,
            CustomAction.DYNAMIC_CHANNEL_MISSION_WIDGET,
            CustomAction.BANNER,
            element.title
        )
        val listPromotions = arrayListOf(
            Promotion(
                creative = creativeName,
                position = creativeSlot,
                id = itemId,
                name = itemName
            )
        )
        return trackingBuilder.constructBasicPromotionView(
            event = Event.PROMO_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.EVENT_ACTION_IMPRESSION,
            eventLabel = Label.NONE,
            promotions = listPromotions
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendUserId(userId)
            .appendCustomKeyValue(CustomAction.TRACKER_ID, CustomAction.TRACKER_ID_IMPRESSION)
            .build()
    }

    fun sendMissionWidgetClickedToPdp(element: CarouselMissionWidgetDataModel, horizontalPosition: Int, userId: String) {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(Action.KEY, CustomAction.EVENT_ACTION_CLICK_PRODUCT)
        bundle.putString(Category.KEY, Category.HOMEPAGE)
        bundle.putString(
            Label.KEY,
            CustomAction.EVENT_LABEL_FORMAT.format(
                element.id,
                element.title
            )
        )
        bundle.putString(CustomAction.TRACKER_ID, CustomAction.TRACKER_ID_CLICKED_PDP)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(UserId.KEY, userId)
        val item = Bundle()
        item.putString(Items.INDEX, (horizontalPosition + 1).toString())
        item.putString(Items.ITEM_BRAND, CustomAction.DEFAULT_VALUE)
        item.putString(
            Items.ITEM_CATEGORY,
            element.categoryID
        )
        item.putString(Items.ITEM_ID, element.productID)
        item.putString(Items.ITEM_NAME, element.productName)
        item.putString(Items.ITEM_VARIANT, CustomAction.DEFAULT_VALUE)
        item.putString(Items.PRICE, CustomAction.DEFAULT_PRICE)
        bundle.putParcelableArrayList(Items.KEY, arrayListOf(item))
        bundle.putString(ItemList.KEY, CustomAction.ITEM_LIST_FORMAT.format(
            element.verticalPosition,
            if (element.isTopads) CustomAction.TOPADS else CustomAction.NON_TOPADS,
            if (element.isCarousel) CustomAction.CAROUSEL else CustomAction.NON_CAROUSEL,
            element.recommendationType,
            element.pageName,
            element.buType,
            element.title
        ))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Ecommerce.PRODUCT_CLICK, bundle)
    }
}
