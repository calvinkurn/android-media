package com.tokopedia.imagepicker_insta.analytic

import com.tokopedia.config.GlobalConfig
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by meyta.taliti on 09/05/23.
 * Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3920
 */
class FeedVideoDepreciationAnalytic @Inject constructor(
    private val userSession: UserSessionInterface,
    private val sessionIris: String
) {

    private val isSellerApp = GlobalConfig.isSellerApp()

    private val currentSite: String
        get() {
            return if (isSellerApp) "tokopediaseller" else "tokopediamarketplace"
        }

    fun openVideoDepreciationBottomSheetEvent() {
        Tracker.Builder()
            .setEvent("openScreen")
            .setCustomProperty("trackerId", if (isSellerApp) "43254" else "43251")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty("sessionIris", sessionIris)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickToShortVideoEvent(isBuyer: Boolean) {
        val partnerId = if (isBuyer) userSession.userId else userSession.shopId
        val label = if (isBuyer) "user" else "seller"
        Tracker.Builder()
            .setEvent("clickContent")
            .setEventAction("click - ke short video")
            .setEventCategory("content feed post creation")
            .setEventLabel("$partnerId - $label")
            .setCustomProperty("trackerId", if (isSellerApp) "43255" else "43252")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty("sessionIris", sessionIris)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendClickOkCloseBottomSheetEvent(isBuyer: Boolean) {
        val partnerId = if (isBuyer) userSession.userId else userSession.shopId
        val label = if (isBuyer) "user" else "seller"
        Tracker.Builder()
            .setEvent("clickContent")
            .setEventAction("click - oke close bottom sheet")
            .setEventCategory("content feed post creation")
            .setEventLabel("$partnerId - $label")
            .setCustomProperty("trackerId", if (isSellerApp) "43256" else "43253")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty("sessionIris", sessionIris)
            .setUserId(userSession.userId)
            .build()
            .send()
    }
}
