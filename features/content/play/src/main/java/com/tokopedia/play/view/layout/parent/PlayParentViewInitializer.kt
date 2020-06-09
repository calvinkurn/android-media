package com.tokopedia.play.view.layout.parent

import android.view.ViewGroup
import androidx.annotation.IdRes

/**
 * Created by jegul on 05/05/20
 */
interface PlayParentViewInitializer {

    @IdRes fun onInitCloseButton(container: ViewGroup): Int
    @IdRes fun onInitVideoFragment(container: ViewGroup): Int
    @IdRes fun onInitUserInteractionFragment(container: ViewGroup): Int
    @IdRes fun onInitMiniInteractionFragment(container: ViewGroup): Int
    @IdRes fun onInitBottomSheetFragment(container: ViewGroup): Int
    @IdRes fun onInitYouTubeFragment(container: ViewGroup): Int
    @IdRes fun onInitErrorFragment(container: ViewGroup): Int
}