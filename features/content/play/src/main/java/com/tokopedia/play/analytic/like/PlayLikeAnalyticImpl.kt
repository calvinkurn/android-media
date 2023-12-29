package com.tokopedia.play.analytic.like

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.Key
import com.tokopedia.play.analytic.*
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.recom.PlayLikeStatus
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by jegul on 23/08/21
 */
class PlayLikeAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : PlayLikeAnalytic {

    private val userId: String
        get() = userSession.userId

    private val isLoggedIn: String
        get() = userSession.isLoggedIn.toString()

    override fun clickLike(
        channelId: String,
        channelType: PlayChannelType,
        channelName: String,
        likeStatus: PlayLikeStatus
    ) {
        val action = if (likeStatus == PlayLikeStatus.Liked) LIKE else UNLIKE
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.clickGroupChat,
                Key.eventAction to "click $action",
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventLabel to "$channelId - ${channelType.value}",
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId,
                Key.isLoggedInStatus to isLoggedIn,
                KEY_CHANNEL to channelName
            )
        )
    }

    companion object {
        private const val LIKE = "like"
        private const val UNLIKE = "unlike"
    }
}
