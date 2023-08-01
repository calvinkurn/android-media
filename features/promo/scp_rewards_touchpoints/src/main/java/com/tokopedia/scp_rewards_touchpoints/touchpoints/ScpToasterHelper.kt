package com.tokopedia.scp_rewards_touchpoints.touchpoints

import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.utils.downloadImage
import com.tokopedia.scp_rewards_touchpoints.touchpoints.ScpRewardsToaster.DEFAULT_DURATION
import com.tokopedia.scp_rewards_touchpoints.touchpoints.analytics.ScpRewardsToasterAnalytics.sendClickToasterEvent
import com.tokopedia.scp_rewards_touchpoints.touchpoints.data.model.ScpToasterData
import com.tokopedia.scp_rewards_touchpoints.touchpoints.data.response.ScpRewardsMedalTouchPointResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object ScpToasterHelper {
    fun showToaster(
        view: View,
        duration: Int = DEFAULT_DURATION,
        data: ScpRewardsMedalTouchPointResponse,
        customBottomHeight: Int = Int.ZERO,
        ctaClickListener: (() -> Unit)? = null
    ) {
        data.scpRewardsMedaliTouchpointOrder.medaliTouchpointOrder.apply {
            val badgeImageUrl = medaliIconImageURL
            val sunflareUrl = medaliSunburstImageURL
            CoroutineScope(Dispatchers.IO).launch {
                val badgeImage = async { view.context.downloadImage(badgeImageUrl) }
                val sunflareImage = async { view.context.downloadImage(sunflareUrl) }
                val toasterData = ScpToasterData(
                    title = infoMessage.title,
                    subtitle = infoMessage.subtitle,
                    ctaText = cta.text,
                    iconImage = badgeImage.await(),
                    sunburstImage = sunflareImage.await(),
                    ctaIsShown = cta.isShown
                )
                withContext(Dispatchers.Main) {
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
                            ctaClickListener?.invoke()
                        }
                    ).show()
                }
            }
        }
    }
}
