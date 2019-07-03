package com.tokopedia.groupchat.room.view.viewstate

import android.view.View
import com.google.android.youtube.player.YouTubePlayer
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

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
        videoContainer.visibility = View.VISIBLE
        setChatListHasSpaceOnTop.invoke(false)
    }

    fun hideVideo() {
        hideVideoToggle.hide()
        showVideoToggle.show()
        youTubePlayer?.pause()
        videoContainer.visibility = View.GONE
        setChatListHasSpaceOnTop.invoke(true)
    }

    fun assignPlayer(youTubePlayer: YouTubePlayer) {
        this.youTubePlayer = youTubePlayer
    }
}