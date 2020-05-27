package com.tokopedia.play.broadcaster.mocker

import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.view.uimodel.FollowerUiModel
import com.tokopedia.play.broadcaster.view.uimodel.SummaryUiModel

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

    fun getSummary(): SummaryUiModel {
        val infos = List(7) { SummaryUiModel.LiveInfo("", "Description $it", (it * 30).toString()) }
        val tickerContent = SummaryUiModel.TickerContent("Live Streaming Berakhir", "Waktu live streaming kamu sudah 30 menit", true)
        return SummaryUiModel(coverImage = "",
                tickerContent = tickerContent ,
                liveTitle = "Sneakers Hypebeast with Cashback 10%",
                liveDuration = "28:42", liveInfos = infos, finishRedirectUrl = "")
    }
}