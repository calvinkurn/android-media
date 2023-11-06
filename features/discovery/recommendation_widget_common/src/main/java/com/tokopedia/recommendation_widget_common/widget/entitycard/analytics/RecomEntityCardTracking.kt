package com.tokopedia.recommendation_widget_common.widget.entitycard.analytics

import android.os.Bundle
import com.tokopedia.recommendation_widget_common.widget.entitycard.model.RecomEntityCardUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.PROMO_CLICK
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.SELECT_CONTENT

object RecomEntityCardTracking : BaseTrackerConst() {

    private const val CLICK_ON_BANNER_FOR_YOU_WIDGET = "click on banner for you widget"
    private const val RECOMMENDATION_CARD_FOR_YOU = "recommendation card for you"
    private const val TRACKER_ID_47716 = "47716"

    fun sendClickEntityCardTracking(
        recomEntityCardUiModel: RecomEntityCardUiModel,
        position: Int,
        userId: String
    ) {
        val bundle = Bundle().apply {
            putString(Event.KEY, SELECT_CONTENT)
            putString(Action.KEY, CLICK_ON_BANNER_FOR_YOU_WIDGET)
            putString(Category.KEY, Category.HOMEPAGE)
            putString(
                Label.KEY,
                "${recomEntityCardUiModel.layoutCard} - ${recomEntityCardUiModel.labelState.title} - ${recomEntityCardUiModel.title}"
            )
            putString(
                TrackerId.KEY,
                TRACKER_ID_47716
            )
            putString(
                BusinessUnit.KEY,
                BusinessUnit.DEFAULT
            )
            putString(
                CurrentSite.KEY,
                CurrentSite.DEFAULT
            )
            putBundle(
                Promotion.KEY,
                Bundle().also {
                    // todo need to confirm later
                    it.putString(Promotion.CREATIVE_NAME, "")
                    it.putString(Promotion.CREATIVE_SLOT, position.toString())
                    // todo need to confirm later
                    it.putString(Promotion.ITEM_ID, recomEntityCardUiModel.id)
                    // todo need to confirm later
                    it.putString(
                        Promotion.ITEM_NAME,
                        "/ - p$position - $RECOMMENDATION_CARD_FOR_YOU - banner - ${recomEntityCardUiModel.layoutCard} - ${recomEntityCardUiModel.labelState.title} - ${recomEntityCardUiModel.title}"
                    )
                }
            )
            putString(UserId.KEY, userId)
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(PROMO_CLICK, bundle)
    }

    fun sendImpressEntityCardTracking(
        recomEntityCardUiModel: RecomEntityCardUiModel,
        position: Int,
        userId: String
    ) {
        val bundle = Bundle().apply {
            putString(Event.KEY, SELECT_CONTENT)
            putString(Action.KEY, CLICK_ON_BANNER_FOR_YOU_WIDGET)
            putString(Category.KEY, Category.HOMEPAGE)
            putString(
                Label.KEY,
                "${recomEntityCardUiModel.layoutCard} - ${recomEntityCardUiModel.labelState.title} - ${recomEntityCardUiModel.title}"
            )
            putString(
                TrackerId.KEY,
                TRACKER_ID_47716
            )
            putString(
                BusinessUnit.KEY,
                BusinessUnit.DEFAULT
            )
            putString(
                CurrentSite.KEY,
                CurrentSite.DEFAULT
            )
            putBundle(
                Promotion.KEY,
                Bundle().also {
                    // todo need to confirm later
                    it.putString(Promotion.CREATIVE_NAME, "")
                    it.putString(Promotion.CREATIVE_SLOT, position.toString())
                    // todo need to confirm later
                    it.putString(Promotion.ITEM_ID, recomEntityCardUiModel.id)
                    // todo need to confirm later
                    it.putString(
                        Promotion.ITEM_NAME,
                        "/ - p$position - $RECOMMENDATION_CARD_FOR_YOU - banner - ${recomEntityCardUiModel.layoutCard} - ${recomEntityCardUiModel.labelState.title} - ${recomEntityCardUiModel.title}"
                    )
                }
            )
            putString(UserId.KEY, userId)
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(PROMO_CLICK, bundle)
    }
}
