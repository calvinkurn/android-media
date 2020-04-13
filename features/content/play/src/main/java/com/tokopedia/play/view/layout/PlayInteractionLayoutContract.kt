package com.tokopedia.play.view.layout

import androidx.annotation.IdRes

/**
 * Created by jegul on 13/04/20
 */
interface PlayInteractionLayoutContract {

    fun layoutView(
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
    )
}