package com.tokopedia.play.view.layout.interaction

import android.view.ViewGroup
import androidx.annotation.IdRes

/**
 * Created by jegul on 20/04/20
 */
interface PlayInteractionViewInitializer {

    @IdRes fun onInitSizeContainer(container: ViewGroup): Int
    @IdRes fun onInitToolbar(container: ViewGroup): Int
    @IdRes fun onInitVideoControl(container: ViewGroup): Int
    @IdRes fun onInitLike(container: ViewGroup): Int
    @IdRes fun onInitChat(container: ViewGroup): Int
    @IdRes fun onInitChatList(container: ViewGroup): Int
    @IdRes fun onInitPinned(container: ViewGroup): Int
    @IdRes fun onInitPlayButton(container: ViewGroup): Int
    @IdRes fun onInitImmersiveBox(container: ViewGroup): Int
    @IdRes fun onInitQuickReply(container: ViewGroup): Int
    @IdRes fun onInitGradientBackground(container: ViewGroup): Int
    @IdRes fun onInitEndLiveComponent(container: ViewGroup): Int
    @IdRes fun onInitStatsInfo(container: ViewGroup): Int
    @IdRes fun onInitVideoSettings(container: ViewGroup): Int
}