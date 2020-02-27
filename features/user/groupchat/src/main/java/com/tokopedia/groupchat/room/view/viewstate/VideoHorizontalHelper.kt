package com.tokopedia.groupchat.room.view.viewstate

import android.view.View
import androidx.fragment.app.FragmentActivity
import com.google.android.youtube.player.YouTubePlayer
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

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
        analytics: GroupChatAnalytics,
        var activity: FragmentActivity
): PlayBaseHelper(model) {

    var remoteConfig: FirebaseRemoteConfigImpl
    init {
        hideVideoToggle.setOnClickListener {
            hideVideo()
            analytics.eventClickHideVideoToggle(viewModel?.channelId)
            setChatListHasSpaceOnTop.invoke(HORIZONTAL_WITHOUT_VIDEO)
        }
        showVideoToggle.setOnClickListener {
            showVideo()
            analytics.eventClickShowVideoToggle(viewModel?.channelId)
            setChatListHasSpaceOnTop.invoke(HORIZONTAL_WITH_VIDEO)
        }
        remoteConfig = FirebaseRemoteConfigImpl(activity)
    }

    fun showVideo() {
        showVideoToggle.hide()
        hideVideoToggle.show()
        videoContainer.show()
    }

    fun hideVideo() {
        hideVideoAndToggle()
        showVideoToggle.show()
    }

    fun assignPlayer(youTubePlayer: YouTubePlayer) {
        this.youTubePlayer = youTubePlayer
        setYoutubeLandscapeEnabled()
    }

    fun hideAllToggle() {
        showVideoToggle.hide()
        hideVideoToggle.hide()
    }

    fun showVideoOnly() {
        showVideo()
        hideAllToggle()
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
    }

    fun clearDataVideoHorizontal() {
        viewModel?.videoId = ""
        viewModel?.isVideoLive = false
    }

    private fun setYoutubeLandscapeEnabled() {
        youTubePlayer?.setShowFullscreenButton(remoteConfig.getBoolean(RemoteConfigKey.PLAY_YOUTUBE_FULL_SCREEN))
    }

    companion object {
        var HORIZONTAL_WITH_VIDEO = 0
        var HORIZONTAL_WITHOUT_VIDEO = 54
    }
}