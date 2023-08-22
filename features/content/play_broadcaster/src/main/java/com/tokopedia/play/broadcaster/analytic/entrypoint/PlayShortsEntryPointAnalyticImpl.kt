package com.tokopedia.play.broadcaster.analytic.entrypoint

import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.Key
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.play.broadcaster.analytic.*
import com.tokopedia.play.broadcaster.analytic.currentSite
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 24, 2022
 */

/**
 * Mynakama Tracker
 * https://mynakama.tokopedia.com/datatracker/requestdetail/view/3511
 */
class PlayShortsEntryPointAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : PlayShortsEntryPointAnalytic {

    /**
     * Row 2
     */
    override fun clickShortsEntryPoint(
        accountId: String,
        accountType: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.clickContent,
                Key.eventAction to "click - buat video",
                Key.eventCategory to KEY_TRACK_CATEGORY_PLAY,
                Key.eventLabel to "$accountId - ${getAccountType(accountType)}",
                Key.currentSite to currentSite,
                Key.userId to userSession.userId,
                Key.businessUnit to BusinessUnit.play,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.trackerId to getTrackerIdBySite("37525", "37607")
            )
        )
    }

    /**
     * Row 69
     */
    override fun clickCloseShortsEntryPointCoachMark(accountId: String, accountType: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.clickContent,
                Key.eventAction to "click - close entry point coachmark",
                Key.eventCategory to KEY_TRACK_CATEGORY_PLAY,
                Key.eventLabel to "$accountId - ${getAccountType(accountType)}",
                Key.currentSite to currentSite,
                Key.userId to userSession.userId,
                Key.businessUnit to BusinessUnit.play,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.trackerId to getTrackerIdBySite("37592", "37674")
            )
        )
    }

    /**
     * Row 80
     */
    override fun viewShortsEntryPoint(accountId: String, accountType: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.viewContentIris,
                Key.eventAction to "view - buat video",
                Key.eventCategory to KEY_TRACK_CATEGORY_PLAY,
                Key.eventLabel to "$accountId - ${getAccountType(accountType)}",
                Key.currentSite to currentSite,
                Key.userId to userSession.userId,
                Key.businessUnit to BusinessUnit.play,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.trackerId to getTrackerIdBySite("37603", "37685")
            )
        )
    }

    private fun getAccountType(accountType: String): String {
        return when (accountType) {
            ContentCommonUserType.TYPE_SHOP -> SHORTS_TYPE_SELLER
            ContentCommonUserType.TYPE_USER -> SHORTS_TYPE_USER
            else -> ""
        }
    }

    private fun getTrackerIdBySite(mainAppTrackerId: String, sellerAppTrackerId: String): String {
        return if (GlobalConfig.isSellerApp()) {
            sellerAppTrackerId
        } else {
            mainAppTrackerId
        }
    }

    companion object {
        private const val SHORTS_TYPE_USER = "user"
        private const val SHORTS_TYPE_SELLER = "seller"
    }
}
