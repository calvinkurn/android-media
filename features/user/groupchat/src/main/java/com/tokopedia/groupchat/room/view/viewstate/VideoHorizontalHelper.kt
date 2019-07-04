package com.tokopedia.groupchat.room.view.viewstate

import android.view.View
import com.google.android.youtube.player.YouTubePlayer
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics
import com.tokopedia.kotlin.extensions.view.hide
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
        private var setChatListHasSpaceOnTop: (Boolean) -> Unit,
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
        setChatListHasSpaceOnTop.invoke(false)
    }

    fun hideVideo() {
        hideVideoToggle.hide()
        showVideoToggle.show()
        youTubePlayer?.pause()
        videoContainer.hide()
        setChatListHasSpaceOnTop.invoke(true)
        liveIndicator.hide()
    }

    fun assignPlayer(youTubePlayer: YouTubePlayer) {
        this.youTubePlayer = youTubePlayer
    }

    fun hideToggle() {
        showVideoToggle.hide()
        hideVideoToggle.hide()
    }

    fun showVideoOnly(videoLive: Boolean) {
        showVideo()
        hideToggle()
        liveIndicator.showWithCondition(videoLive)
    }
}