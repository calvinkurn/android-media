package com.tokopedia.tokofood.feature.home.analytics

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.EVENT_ACTION_CLICK_CATEGORY_ICONS
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.EMPTY_DATA
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.GOFOOD_PAGENAME
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.VIEW_ITEM
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeLayoutType
import com.tokopedia.tokofood.feature.home.domain.data.DynamicIcon
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.SELECT_CONTENT

/**
 * Home
 * https://mynakama.tokopedia.com/datatracker/requestdetail/view/3053
 */
class TokoFoodHomeAnalytics: BaseTrackerConst() {

    fun clickLCAWidget(userId: String?, destinationId: String?) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, TokoFoodAnalytics.EVENT_ACTION_CLICK_LCA)
            putString(TrackAppUtils.EVENT, "")
        }
        eventDataLayer.clickPG(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(TokoFoodAnalyticsConstants.CLICK_PG, eventDataLayer)
    }

    fun impressionIconWidget(userId: String?, destinationId: String?, data: List<DynamicIcon>, verticalPosition: Int) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION,  EVENT_ACTION_CLICK_CATEGORY_ICONS)
            putString(TrackAppUtils.EVENT, "")
        }
        eventDataLayer.putParcelableArrayList(Promotion.KEY, getPromotionItem(data, verticalPosition = verticalPosition))
        eventDataLayer.viewItem(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM, eventDataLayer)
    }

    fun clickIconWidget(userId: String?, destinationId: String?, data: List<DynamicIcon>, horizontalPosition: Int, verticalPosition: Int) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION,  EVENT_ACTION_CLICK_CATEGORY_ICONS)
            putString(TrackAppUtils.EVENT, "")
        }
        eventDataLayer.putParcelableArrayList(Promotion.KEY, getPromotionItem(data, horizontalPosition, verticalPosition))
        eventDataLayer.selectContent(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventDataLayer)
    }

    private fun getPromotionItem(data: List<DynamicIcon>, horizontalPosition: Int = -1, verticalPosition: Int): ArrayList<Bundle> {
        val promotionBundle = arrayListOf<Bundle>()
        promotionBundle.addAll(
            data.mapIndexed { index, it ->
                val position = if (horizontalPosition.isLessThanZero()) index else horizontalPosition
                Bundle().apply {
                    putString(Promotion.CREATIVE_NAME, "${it.imageUrl} - ${it.applinks}")
                    putString(Promotion.CREATIVE_SLOT, (position + 1).toString())
                    putString(Promotion.ITEM_ID, it.name)
                    putString(Promotion.ITEM_NAME, "$GOFOOD_PAGENAME - ${TokoFoodHomeLayoutType.ICON_TOKOFOOD} - ${verticalPosition} - $EMPTY_DATA")
                }
            }
        )
        return promotionBundle
    }

    private fun Bundle.viewItem(userId: String?, destinationId: String?): Bundle {
        addGeneralTracker(userId, destinationId)
        this.putString(TrackAppUtils.EVENT, VIEW_ITEM)
        return this
    }

    private fun Bundle.clickPG(userId: String?, destinationId: String?): Bundle {
        addGeneralTracker(userId, destinationId)
        this.putString(TrackAppUtils.EVENT, TokoFoodAnalyticsConstants.CLICK_PG)
        return this
    }

    private fun Bundle.selectContent(userId: String?, destinationId: String?): Bundle {
        addGeneralTracker(userId, destinationId)
        this.putString(TrackAppUtils.EVENT, SELECT_CONTENT)
        return this
    }

    private fun Bundle.addGeneralTracker(userId: String?, destinationId: String?): Bundle {
        this.putString(TrackAppUtils.EVENT_CATEGORY, TokoFoodAnalytics.EVENT_CATEGORY_HOME_PAGE)
        this.putString(TokoFoodAnalyticsConstants.BUSSINESS_UNIT, TokoFoodAnalyticsConstants.PHYSICAL_GOODS)
        this.putString(TokoFoodAnalyticsConstants.CURRENT_SITE, TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE)
        this.putString(TokoFoodAnalyticsConstants.USER_ID, userId ?: EMPTY_DATA)
        this.putString(TokoFoodAnalyticsConstants.DESTINATION_ID, destinationId ?: EMPTY_DATA)
        return this
    }
}