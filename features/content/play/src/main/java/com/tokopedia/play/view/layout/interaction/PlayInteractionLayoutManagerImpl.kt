package com.tokopedia.play.view.layout.interaction

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.core.view.WindowInsetsCompat
import com.tokopedia.play.view.layout.PlayLayoutManager
import com.tokopedia.play.view.type.ScreenOrientation

/**
 * Created by jegul on 01/04/20
 */
class PlayInteractionLayoutManagerImpl(
        context: Context,
        private val orientation: ScreenOrientation,
        @IdRes private val sizeContainerComponentId: Int,
        @IdRes private val sendChatComponentId: Int,
        @IdRes private val likeComponentId: Int,
        @IdRes private val pinnedComponentId: Int,
        @IdRes private val chatListComponentId: Int,
        @IdRes private val videoControlComponentId: Int,
        @IdRes private val gradientBackgroundComponentId: Int,
        @IdRes private val toolbarComponentId: Int,
        @IdRes private val statsInfoComponentId: Int,
        @IdRes private val playButtonComponentId: Int,
        @IdRes private val immersiveBoxComponentId: Int,
        @IdRes private val quickReplyComponentId: Int,
        @IdRes private val endLiveInfoComponentId: Int,
        @IdRes private val videoSettingsComponentId: Int
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

    private fun getManager(): PlayInteractionLayoutManager = when (orientation) {
        ScreenOrientation.Portrait, ScreenOrientation.Unknown -> portraitManager
        ScreenOrientation.Landscape -> landscapeManager
    }
}