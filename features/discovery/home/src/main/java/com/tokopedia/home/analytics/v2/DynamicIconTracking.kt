package com.tokopedia.home.analytics.v2


import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import java.util.*

/**
 * Created by Lukas on 1/15/21.
 */
object DynamicIconTracking : BaseTracking(){
    private const val DYNAMIC_ICON = "dynamic icon"
    private const val DYNAMIC_ICON_PROMOTION_NAME = "/ - p%s - dynamic icon"
    fun sendDynamicIconImpress(userId: String, dynamicIcons: List<DynamicIconComponent.DynamicIcon>, position: Int, type: Int){
        val tracker = BaseTrackerBuilder().constructBasicPromotionView(
                event = Event.PROMO_VIEW,
                eventAction = Action.IMPRESSION_ON.format(DYNAMIC_ICON),
                eventCategory = Category.HOMEPAGE,
                eventLabel = Label.NONE,
                promotions = dynamicIcons.mapIndexed { index, dynamicIcon ->
                    BaseTrackerConst.Promotion(
                            id = dynamicIcon.id,
                            name = DYNAMIC_ICON_PROMOTION_NAME.format(position),
                            position = type.toString() + " - " + (index + 1).toString(),
                            creative = dynamicIcon.businessUnitIdentifier,
                            creativeUrl = dynamicIcon.imageUrl
                    )
                }
        )
        .appendBusinessUnit(BusinessUnit.DEFAULT)
        .appendCurrentSite(CurrentSite.DEFAULT)
        .appendUserId(userId)
        .build()
        getTracker().sendEnhanceEcommerceEvent(tracker as HashMap<String, Any>)
    }

    fun sendDynamicIconClick(userId: String, dynamicIcon: DynamicIconComponent.DynamicIcon, position: Int, iconPosition: Int, type: Int){
        val tracker = BaseTrackerBuilder().constructBasicPromotionClick(
                event = Event.PROMO_CLICK,
                eventAction = Action.CLICK_ON.format(DYNAMIC_ICON),
                eventCategory = Category.HOMEPAGE,
                eventLabel = dynamicIcon.name,
                promotions = listOf(
                        BaseTrackerConst.Promotion(
                                id = dynamicIcon.id,
                                name = DYNAMIC_ICON_PROMOTION_NAME.format(position),
                                position = type.toString() + " - " + (iconPosition + 1).toString(),
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
        getTracker().sendEnhanceEcommerceEvent(tracker)
    }
}