package com.tokopedia.play.broadcaster.mocker

import com.tokopedia.play.broadcaster.view.uimodel.FollowerUiModel

/**
 * Created by jegul on 20/05/20
 */
object PlayBroadcastMocker {

    fun getMockUnknownFollower() = List(3) { FollowerUiModel.Unknown }
    fun getMockUserFollower() = List(3) { FollowerUiModel.User("https://www.tokopedia.com") }
}