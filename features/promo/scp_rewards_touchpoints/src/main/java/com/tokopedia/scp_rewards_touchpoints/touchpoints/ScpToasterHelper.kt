package com.tokopedia.scp_rewards_touchpoints.touchpoints

import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.scp_rewards_touchpoints.touchpoints.ScpRewardsToaster.DEFAULT_DURATION
import com.tokopedia.scp_rewards_touchpoints.touchpoints.analytics.ScpRewardsToasterAnalytics.sendClickToasterEvent
import com.tokopedia.scp_rewards_touchpoints.touchpoints.model.ScpRewardsMedaliTouchPointModel
import com.tokopedia.scp_rewards_touchpoints.touchpoints.model.ScpToasterData

object ScpToasterHelper {
    fun showToaster(
        view: View,
        duration:Int = DEFAULT_DURATION,
        data: ScpRewardsMedaliTouchPointModel
    ) {
        data.scpRewardsMedaliTouchpointOrder.medaliTouchpointOrder.apply {
            val title = infoMessage.title
            val subtitle =  infoMessage.subtitle
            val ctaTitle =  cta.text
            val badgeImageUrl = medaliIconImageURL
            val sunflareUrl = medaliSunburstImageURL
            val toasterData = ScpToasterData(
                title = title,
                subtitle = subtitle,
                ctaTitle = ctaTitle,
                badgeImage = badgeImageUrl,
                sunflare = sunflareUrl,
                ctaIsShown = cta.isShown
            )
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
