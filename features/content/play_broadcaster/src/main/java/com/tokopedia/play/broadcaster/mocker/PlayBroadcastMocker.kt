package com.tokopedia.play.broadcaster.mocker

import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.view.uimodel.FollowerUiModel

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
}