package com.tokopedia.scp_rewards_touchpoints.touchpoints

import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.scp_rewards_touchpoints.touchpoints.ScpRewardsToaster.DEFAULT_DURATION
import com.tokopedia.scp_rewards_touchpoints.touchpoints.analytics.ScpRewardsToasterAnalytics.sendClickToasterEvent
import com.tokopedia.scp_rewards_touchpoints.touchpoints.data.response.ScpRewardsMedalTouchPointResponse
import com.tokopedia.scp_rewards_touchpoints.touchpoints.data.model.ScpToasterData

object ScpToasterHelper {
    fun showToaster(
        view: View,
        duration:Int = DEFAULT_DURATION,
        data: ScpRewardsMedalTouchPointResponse,
        customBottomHeight: Int = Int.ZERO
    ) {
        data.scpRewardsMedaliTouchpointOrder.medaliTouchpointOrder.apply {
            val toasterData = ScpToasterData(
                title = infoMessage.title,
                subtitle = infoMessage.subtitle,
                iconImage = medaliIconImageURL,
                sunburstImage = medaliSunburstImageURL,
                ctaText = cta.text,
                ctaIsShown = cta.isShown
            )
            ScpRewardsToaster.toasterCustomBottomHeight = customBottomHeight
            ScpRewardsToaster.build(
                view = view,
                toasterData = toasterData,
                duration = duration,
                clickListener = {
                    sendClickToasterEvent(
                        badgeId = medaliID.toString()
                    )
                    RouteManager.route(view.context, cta.appLink)
                }
            ).show()
        }
    }

}
