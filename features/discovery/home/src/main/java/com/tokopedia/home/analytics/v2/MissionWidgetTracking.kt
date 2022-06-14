package com.tokopedia.home.analytics.v2

import android.os.Bundle
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMissionWidgetDataModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * Created by dhaba
 */
object MissionWidgetTracking : BaseTrackerConst() {
    private class CustomAction {
        companion object {
            const val EVENT_ACTION_CLICK = "click on banner dynamic channel mission widget"
            const val EVENT_LABEL_FORMAT = "%s - %s"
            const val TRACKER_ID = "trackerId"
            const val TRACKER_ID_CLICKED = "32188"
            const val DEFAULT_VALUE = ""
            const val ITEM_ID_FORMAT = "%s_%s_%s_%s"
            const val DYNAMIC_CHANNEL_MISSION_WIDGET = "dynamic channel mission widget"
            const val BANNER = "banner"
            const val ITEM_NAME_FORMAT = "/ - p%s - %s - %s - %s"
        }
    }

    fun getMissionWidgetClicked(element: CarouselMissionWidgetDataModel, horizontalPosition: Int, userId: String) {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(Action.KEY, CustomAction.EVENT_ACTION_CLICK)
        bundle.putString(Category.KEY, Category.HOMEPAGE)
        bundle.putString(
            Label.KEY,
            CustomAction.EVENT_LABEL_FORMAT.format(
                element.channel.id,
                element.channel.channelHeader.name
            )
        )
        bundle.putString(CustomAction.TRACKER_ID, CustomAction.TRACKER_ID_CLICKED)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CampaignCode.KEY, element.channel.trackingAttributionModel.campaignCode)
        bundle.putString(Label.CHANNEL_LABEL, element.channel.id)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(UserId.KEY, userId)
        val promotion = Bundle()
        promotion.putString(Promotion.CREATIVE_NAME, CustomAction.DEFAULT_VALUE)
        promotion.putString(Promotion.CREATIVE_SLOT, (horizontalPosition + 1).toString())
        promotion.putString(
            Promotion.ITEM_ID,
            CustomAction.ITEM_ID_FORMAT.format(
                element.channel.id,
                element.id,
                CustomAction.DEFAULT_VALUE,
                CustomAction.DEFAULT_VALUE
            )
        )
        promotion.putString(
            Promotion.ITEM_NAME,
            CustomAction.ITEM_NAME_FORMAT.format(
                element.verticalPosition,
                CustomAction.DYNAMIC_CHANNEL_MISSION_WIDGET,
                CustomAction.BANNER,
                element.channel.channelHeader.name
            )
        )
        bundle.putParcelableArrayList(Promotion.KEY, arrayListOf(promotion))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Ecommerce.PROMO_CLICK, bundle)
    }
}