package com.tokopedia.play.broadcaster.mocker

import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.data.model.Configuration
import com.tokopedia.play.broadcaster.view.uimodel.ChannelInfoUiModel
import com.tokopedia.play.broadcaster.view.uimodel.FollowerUiModel
import com.tokopedia.play.broadcaster.view.uimodel.PlayChannelStatus

/**
 * Created by jegul on 20/05/20
 */
object PlayBroadcastMocker {

    fun getMockUnknownFollower() = List(3) {
        FollowerUiModel.Unknown(when (it) {
            0 -> R.color.play_follower_orange
            1 -> R.color.play_follower_blue
            else -> R.color.play_follower_yellow
        })
    }
    fun getMockUserFollower() = List(3) { FollowerUiModel.User("https://www.tokopedia.com") }

    fun getMockConfiguration() = Configuration(
            isUserWhitelisted = true,
            isHaveOnGoingLive = false,
            isOfficial = false,
            channelId = "",
            maxTaggedProduct = 15,
            maxLiveStreamDuration = (60*1000)*30,
            countDownDuration = 10
    )

    fun getMockActiveChannel() = ChannelInfoUiModel(
            ingestUrl = "rtmp://192.168.0.105/live/ByNDo1ds8",
            shareUrl = "tokopedia://play/2214",
            status = PlayChannelStatus.Active
    )
}