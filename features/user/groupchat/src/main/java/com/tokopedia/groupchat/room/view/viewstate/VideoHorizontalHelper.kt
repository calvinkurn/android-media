package com.tokopedia.groupchat.room.view.viewstate

import android.content.res.Configuration
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.FragmentActivity
import com.google.android.youtube.player.YouTubePlayer
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

/**
 * @author : Steven 28/05/19
 */
class VideoHorizontalHelper(
        model: ChannelInfoViewModel?,
        private val layout: ConstraintLayout,
        private var hideVideoToggle: View,
        private var showVideoToggle: View,
        private var videoContainer: View,
        private var youTubePlayer: YouTubePlayer?,
        private var setChatListHasSpaceOnTop: (Int) -> Unit,
        analytics: GroupChatAnalytics,
        var activity: FragmentActivity,
        private val toolbar: Toolbar
): PlayBaseHelper(model) {

    private val currentOrientation: Int
        get() = activity.resources.configuration.orientation

    private val isLandscape: Boolean
        get() = currentOrientation == Configuration.ORIENTATION_LANDSCAPE

    private val isPortrait: Boolean
        get() = currentOrientation == Configuration.ORIENTATION_PORTRAIT

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
        if (isPortrait) hideVideoToggle.show()
        videoContainer.show()
    }

    fun hideVideo() {
        hideVideoAndToggle()
        if (isPortrait) showVideoToggle.show()
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
        if (!showVideoToggle.isShown && videoContainer.isVisible && isPortrait) {
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

    fun onOrientationChanged(orientation: Int) {
        youTubePlayer?.fullscreenControlFlags = getYoutubeFullScreenControlFlags(orientation)
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) doOnLandscape()
        else doOnPortrait()
    }

    fun getYoutubeFullScreenControlFlags(orientation: Int): Int {
        return if (orientation == Configuration.ORIENTATION_PORTRAIT) YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT
        else (YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT or YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI)
    }

    fun exitFullScreen() {
        youTubePlayer?.setFullscreen(false)
    }

    private fun setYoutubeLandscapeEnabled() {
        youTubePlayer?.setShowFullscreenButton(remoteConfig.getBoolean(RemoteConfigKey.PLAY_YOUTUBE_FULL_SCREEN))
    }

    private fun doOnLandscape() {
        val uiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE)

        activity.window.decorView.systemUiVisibility = uiVisibility

        activity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        toolbar.gone()
        showVideoToggle.gone()
        hideVideoToggle.gone()
        val layoutParams = videoContainer.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        videoContainer.layoutParams = layoutParams
    }

    private fun doOnPortrait() {
        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        toolbar.show()
        showVideoToggle.gone()
        hideVideoToggle.show()
        val layoutParams = videoContainer.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.height = 0
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        videoContainer.layoutParams = layoutParams
    }

    companion object {
        var HORIZONTAL_WITH_VIDEO = 0
        var HORIZONTAL_WITHOUT_VIDEO = 54
    }
}