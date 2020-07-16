package com.tokopedia.play.view.layout.video

import android.view.ViewGroup
import androidx.annotation.IdRes

/**
 * Created by jegul on 24/04/20
 */
interface PlayVideoViewInitializer {

    @IdRes fun onInitVideo(container: ViewGroup): Int
    @IdRes fun onInitVideoLoading(container: ViewGroup): Int
    @IdRes fun onInitOneTap(container: ViewGroup): Int
    @IdRes fun onInitOverlayVideo(container: ViewGroup): Int
}