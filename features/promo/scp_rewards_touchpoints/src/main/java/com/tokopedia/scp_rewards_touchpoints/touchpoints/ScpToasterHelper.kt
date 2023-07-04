package com.tokopedia.scp_rewards_touchpoints.touchpoints

import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.utils.downloadImage
import com.tokopedia.scp_rewards_touchpoints.touchpoints.model.ScpRewardsMedaliTouchPointModel
import com.tokopedia.scp_rewards_touchpoints.touchpoints.model.ScpToasterData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object ScpToasterHelper {

    fun showToaster(view: View,duration:Int = ScpRewardsToaster.DEFAULT_DURATION,data: ScpRewardsMedaliTouchPointModel?){
        val title = data?.scpRewardsMedaliTouchpointOrder?.medaliTouchpointOrder?.infoMessage?.title ?: ""
        val subtitle =  data?.scpRewardsMedaliTouchpointOrder?.medaliTouchpointOrder?.infoMessage?.subtitle ?: ""
        val ctaTitle =  data?.scpRewardsMedaliTouchpointOrder?.medaliTouchpointOrder?.cta?.text ?: ""
        val badgeUrl = data?.scpRewardsMedaliTouchpointOrder?.medaliTouchpointOrder?.medaliIconImageURL ?: ""
        val sunflareUrl = data?.scpRewardsMedaliTouchpointOrder?.medaliTouchpointOrder?.medaliSunburstImageURL ?: ""
        val appLink = data?.scpRewardsMedaliTouchpointOrder?.medaliTouchpointOrder?.cta?.appLink

        CoroutineScope(Dispatchers.IO).launch{
           val badgeImage = async { view.context.downloadImage(badgeUrl)  }
            val sunflare = async { view.context.downloadImage(sunflareUrl) }
            val toasterData = ScpToasterData(
                title = title,
                subtitle = subtitle,
                ctaTitle = ctaTitle,
                badgeImage = badgeImage.await(),
                sunflare = sunflare.await()
            )
            withContext(Dispatchers.Main){
                ScpRewardsToaster.build(view, toasterData, duration, clickListener = {
                    RouteManager.route(view.context, appLink)
                }).show()
            }
        }
    }

}
