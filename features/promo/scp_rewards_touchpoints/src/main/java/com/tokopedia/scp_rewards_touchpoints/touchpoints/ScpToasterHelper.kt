package com.tokopedia.scp_rewards_touchpoints.touchpoints

import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.utils.downloadImage
import com.tokopedia.scp_rewards_touchpoints.touchpoints.ScpRewardsToaster.DEFAULT_DURATION
import com.tokopedia.scp_rewards_touchpoints.touchpoints.analytics.ScpRewardsToasterAnalytics.sendClickToasterEvent
import com.tokopedia.scp_rewards_touchpoints.touchpoints.model.ScpRewardsMedaliTouchPointModel
import com.tokopedia.scp_rewards_touchpoints.touchpoints.model.ScpToasterData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object ScpToasterHelper {
    fun showToaster(
        view: View,
        duration: Int = DEFAULT_DURATION,
        data: ScpRewardsMedaliTouchPointModel
    ) {
        data.scpRewardsMedaliTouchpointOrder.medaliTouchpointOrder.apply {
            val title = infoMessage.title
            val subtitle = infoMessage.subtitle
            val ctaTitle = cta.text
            val badgeImageUrl = medaliIconImageURL
            val sunflareUrl = medaliSunburstImageURL
            CoroutineScope(Dispatchers.IO).launch {
                val badgeImage = async { view.context.downloadImage(badgeImageUrl) }
                val sunflareImage = async { view.context.downloadImage(sunflareUrl) }
                val toasterData = ScpToasterData(
                    title = title,
                    subtitle = subtitle,
                    ctaTitle = ctaTitle,
                    badgeImage = badgeImage.await(),
                    sunflare = sunflareImage.await(),
                    ctaIsShown = cta.isShown
                )
                withContext(Dispatchers.Main) {
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
    }
}
