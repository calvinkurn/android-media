package com.tokopedia.groupchat.room.view.viewstate

import android.view.View
import com.google.android.youtube.player.YouTubePlayer
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition

/**
 * @author : Steven 28/05/19
 */
class VideoHorizontalHelper(
        model: ChannelInfoViewModel?,
        private var hideVideoToggle: View,
        private var showVideoToggle: View,
        private var videoContainer: View,
        private var youTubePlayer: YouTubePlayer?,
        private var setChatListHasSpaceOnTop: (Int) -> Unit,
        private var liveIndicator: View,
        analytics: GroupChatAnalytics
): PlayBaseHelper(model) {

    init {
        hideVideoToggle.setOnClickListener {
            hideVideo()
            analytics.eventClickHideVideoToggle(viewModel?.channelId)
        }
        showVideoToggle.setOnClickListener {
            showVideo()
            analytics.eventClickShowVideoToggle(viewModel?.channelId)
        }
    }

    fun showVideo() {
        showVideoToggle.hide()
        hideVideoToggle.show()
        videoContainer.show()
        setChatListHasSpaceOnTop.invoke(HORIZONTAL_WITH_VIDEO)
    }

    fun hideVideo() {
        setChatListHasSpaceOnTop(VideoHorizontalHelper.HORIZONTAL_WITHOUT_VIDEO)
        hideVideoAndToggle()
        showVideoToggle.show()
    }

    fun assignPlayer(youTubePlayer: YouTubePlayer) {
        this.youTubePlayer = youTubePlayer
    }

    fun hideAllToggle() {
        showVideoToggle.hide()
        hideVideoToggle.hide()
    }

    fun showVideoOnly(videoLive: Boolean) {
        showVideo()
        hideAllToggle()
        liveIndicator.showWithCondition(videoLive)
    }

    fun onPlayed() {
        hideVideoToggle.hide()
    }

    fun onPaused() {
        if (!showVideoToggle.isShown && videoContainer.isVisible) {
            hideVideoToggle.show()
        }
    }

    fun hideVideoAndToggle() {
        try {
            youTubePlayer?.pause()
        }catch (e: Exception) {
            e.printStackTrace()
        }

        hideAllToggle()
        videoContainer.hide()
        liveIndicator.hide()
    }

    companion object {
        var HORIZONTAL_WITH_VIDEO = 0
        var HORIZONTAL_WITHOUT_VIDEO = 54
    }
}