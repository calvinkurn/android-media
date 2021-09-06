package com.tokopedia.play.analytic.like

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
        private val userSession: UserSessionInterface,
) : PlayLikeAnalytic {

    private val userId: String
        get() = userSession.userId

    private val isLoggedIn: String
        get() = userSession.isLoggedIn.toString()

    override fun clickLike(
            channelId: String,
            channelType: PlayChannelType,
            channelName: String,
            likeStatus: PlayLikeStatus,
    ) {
        val action = if(likeStatus == PlayLikeStatus.Liked) LIKE else UNLIKE
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to KEY_TRACK_CLICK_GROUP_CHAT,
                        KEY_EVENT_ACTION to "$KEY_TRACK_CLICK $action",
                        KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                        KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                        KEY_USER_ID to userId,
                        KEY_IS_LOGGED_IN_STATUS to isLoggedIn,
                        KEY_CHANNEL to channelName
                )
        )
    }

    companion object {
        private const val LIKE = "like"
        private const val UNLIKE = "unlike"
    }
}