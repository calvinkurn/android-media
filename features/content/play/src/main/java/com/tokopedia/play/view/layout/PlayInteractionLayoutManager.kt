package com.tokopedia.play.view.layout

import android.view.ViewGroup
import androidx.annotation.IdRes
import com.tokopedia.play.view.type.ScreenOrientation

/**
 * Created by jegul on 01/04/20
 */
class PlayInteractionLayoutManager(
        private val orientation: ScreenOrientation,
        private val container: ViewGroup
) : PlayInteractionLayoutContract {

    override fun layoutView(
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
            @IdRes endLiveInfoComponentId: Int
    ) {
        when (orientation) {
            ScreenOrientation.Portrait, ScreenOrientation.Unknown -> PlayInteractionPortraitManager(container)
            ScreenOrientation.Landscape -> PlayInteractionLandscapeManager(container)
        }.layoutView(
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
                endLiveInfoComponentId = endLiveInfoComponentId
        )
    }


}