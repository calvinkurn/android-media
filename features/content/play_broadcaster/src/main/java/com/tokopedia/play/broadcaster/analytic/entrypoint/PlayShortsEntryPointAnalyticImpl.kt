package com.tokopedia.play.broadcaster.analytic.entrypoint

import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.analytic.*
import com.tokopedia.play.broadcaster.analytic.KEY_BUSINESS_UNIT
import com.tokopedia.play.broadcaster.analytic.KEY_CURRENT_SITE
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT_ACTION
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT_CATEGORY
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT_LABEL
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_BUSINESS_UNIT
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_CATEGORY
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_CLICK_EVENT
import com.tokopedia.play.broadcaster.analytic.KEY_USER_ID
import com.tokopedia.play.broadcaster.analytic.currentSite
import com.tokopedia.play.broadcaster.shorts.analytic.PlayShortsAnalyticImpl
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
    private val userSession: UserSessionInterface,
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
                KEY_EVENT to KEY_TRACK_CLICK_EVENT_SELLER,
                KEY_EVENT_ACTION to "click - buat video",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_PLAY,
                KEY_EVENT_LABEL to "$accountId - ${getAccountType(accountType)}",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_TRACKER_ID to "37525"
            )
        )
    }

    /**
     * Row 69
     */
    override fun clickCloseShortsEntryPointCoachMark(accountId: String, accountType: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT_SELLER,
                KEY_EVENT_ACTION to "click - close entry point coachmark",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_PLAY,
                KEY_EVENT_LABEL to "$accountId - ${getAccountType(accountType)}",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_TRACKER_ID to "37592"
            )
        )
    }

    /**
     * Row 80
     */
    override fun viewShortsEntryPoint(accountId: String, accountType: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_EVENT_SELLER,
                KEY_EVENT_ACTION to "view - buat video",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_PLAY,
                KEY_EVENT_LABEL to "$accountId - ${getAccountType(accountType)}",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_TRACKER_ID to "37603"
            )
        )
    }

    private fun getAccountType(accountType: String): String {
        return when(accountType) {
            ContentCommonUserType.TYPE_SHOP -> SHORTS_TYPE_SELLER
            ContentCommonUserType.TYPE_USER -> SHORTS_TYPE_USER
            else -> ""
        }
    }

    companion object {
        private const val SHORTS_TYPE_USER = "user"
        private const val SHORTS_TYPE_SELLER = "seller"
    }
}
