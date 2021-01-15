package com.tokopedia.home.analytics.v2


import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.HashMap

/**
 * Created by Lukas on 1/15/21.
 */
object DynamicIconTracking : BaseTracking(){
    private const val DYNAMIC_ICON = "dynamic icon"
    private const val DYNAMIC_ICON_PROMOTION_NAME = "/ - p%s - dynamic icon"
    fun sendDynamicIconImpress(trackingQueue: TrackingQueue, userId: String, dynamicIcons: List<DynamicIconComponent.DynamicIcon>, position: Int){
        val tracker = BaseTrackerBuilder().constructBasicPromotionView(
                event = Event.PROMO_VIEW,
                eventAction = Action.IMPRESSION_ON.format(DYNAMIC_ICON),
                eventCategory = Category.HOMEPAGE,
                eventLabel = Label.NONE,
                promotions = dynamicIcons.mapIndexed { index, dynamicIcon ->
                    BaseTrackerConst.Promotion(
                            id = dynamicIcon.id,
                            name = DYNAMIC_ICON_PROMOTION_NAME.format(position),
                            position = (position + 1).toString() + " - " + (index + 1).toString(),
                            creative = dynamicIcon.businessUnitIdentifier,
                            creativeUrl = dynamicIcon.imageUrl
                    )
                }
        )
        .appendBusinessUnit(BusinessUnit.DEFAULT)
        .appendCurrentSite(CurrentSite.DEFAULT)
        .appendUserId(userId)
        .build()
        trackingQueue.putEETracking(tracker as HashMap<String, Any>)
    }

    fun sendDynamicIconClick(userId: String, dynamicIcon: DynamicIconComponent.DynamicIcon, position: Int, iconPosition: Int){
        val tracker = BaseTrackerBuilder().constructBasicPromotionView(
                event = Event.PROMO_CLICK,
                eventAction = Action.CLICK_ON.format(DYNAMIC_ICON),
                eventCategory = Category.HOMEPAGE,
                eventLabel = dynamicIcon.name,
                promotions = listOf(
                        BaseTrackerConst.Promotion(
                                id = dynamicIcon.id,
                                name = DYNAMIC_ICON_PROMOTION_NAME.format(position),
                                position = position.toString() + " - " + (iconPosition + 1).toString(),
                                creative = dynamicIcon.businessUnitIdentifier,
                                creativeUrl = dynamicIcon.imageUrl
                        )
                )
        )
        .appendBusinessUnit(BusinessUnit.DEFAULT)
        .appendCurrentSite(CurrentSite.DEFAULT)
        .appendUserId(userId)
        .appendAffinity(dynamicIcon.persona)
        .appendAttribution(dynamicIcon.galaxyAttribution)
        .appendCampaignCode(dynamicIcon.campaignCode)
        .appendCategoryId(dynamicIcon.categoryPersona)
        .appendShopId(dynamicIcon.brandId)
        .build()
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(tracker)
    }
}