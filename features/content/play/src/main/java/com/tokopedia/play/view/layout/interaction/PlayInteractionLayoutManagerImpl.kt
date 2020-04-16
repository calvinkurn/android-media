package com.tokopedia.play.view.layout.interaction

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.core.view.WindowInsetsCompat
import com.tokopedia.play.view.layout.PlayLayoutManager
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.VideoOrientation

/**
 * Created by jegul on 01/04/20
 */
class PlayInteractionLayoutManagerImpl(
        context: Context,
        private val orientation: ScreenOrientation,
        @IdRes sizeContainerComponentId: Int,
        @IdRes sendChatComponentId: Int,
        @IdRes likeComponentId: Int,
        @IdRes pinnedComponentId: Int,
        @IdRes chatListComponentId: Int,
        @IdRes videoControlComponentId: Int,
        @IdRes gradientBackgroundComponentId: Int,
        @IdRes toolbarComponentId: Int,
        @IdRes statsInfoComponentId: Int,
        @IdRes playButtonComponentId: Int,
        @IdRes immersiveBoxComponentId: Int,
        @IdRes quickReplyComponentId: Int,
        @IdRes endLiveInfoComponentId: Int,
        @IdRes videoSettingsComponentId: Int
) : PlayInteractionLayoutManager {

    private val portraitManager = PlayInteractionPortraitManager(
            context = context,
            sizeContainerComponentId = sizeContainerComponentId,
            sendChatComponentId = sendChatComponentId,
            likeComponentId = likeComponentId,
            pinnedComponentId = pinnedComponentId,
            chatListComponentId = chatListComponentId,
            videoControlComponentId = videoControlComponentId,
            gradientBackgroundComponentId = gradientBackgroundComponentId,
            toolbarComponentId = toolbarComponentId,
            statsInfoComponentId = statsInfoComponentId,
            playButtonComponentId = playButtonComponentId,
            immersiveBoxComponentId = immersiveBoxComponentId,
            quickReplyComponentId = quickReplyComponentId,
            endLiveInfoComponentId = endLiveInfoComponentId,
            videoSettingsComponentId = videoSettingsComponentId
    )
    private val landscapeManager = PlayInteractionLandscapeManager(
            context = context,
            sizeContainerComponentId = sizeContainerComponentId,
            sendChatComponentId = sendChatComponentId,
            likeComponentId = likeComponentId,
            pinnedComponentId = pinnedComponentId,
            chatListComponentId = chatListComponentId,
            videoControlComponentId = videoControlComponentId,
            gradientBackgroundComponentId = gradientBackgroundComponentId,
            toolbarComponentId = toolbarComponentId,
            statsInfoComponentId = statsInfoComponentId,
            playButtonComponentId = playButtonComponentId,
            immersiveBoxComponentId = immersiveBoxComponentId,
            quickReplyComponentId = quickReplyComponentId,
            endLiveInfoComponentId = endLiveInfoComponentId,
            videoSettingsComponentId = videoSettingsComponentId
    )

    override fun layoutView(view: View) {
        getManager().layoutView(view)
    }

    override fun setupInsets(view: View, insets: WindowInsetsCompat) {
        getManager().setupInsets(view, insets)
    }

    override fun onDestroy() {
        getManager().onDestroy()
    }

    override fun onEnterImmersive(): Int {
        return getManager().onEnterImmersive()
    }

    override fun onExitImmersive(): Int {
        return getManager().onExitImmersive()
    }

    override fun onVideoOrientationChanged(container: View, videoOrientation: VideoOrientation) {
        return getManager().onVideoOrientationChanged(container, videoOrientation)
    }

    private fun getManager(): PlayInteractionLayoutManager = when (orientation) {
        ScreenOrientation.Portrait, ScreenOrientation.ReversedPortrait, ScreenOrientation.Unknown -> portraitManager
        ScreenOrientation.Landscape, ScreenOrientation.ReversedLandscape -> landscapeManager
    }
}