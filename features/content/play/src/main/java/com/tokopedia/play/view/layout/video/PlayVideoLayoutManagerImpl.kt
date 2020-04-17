package com.tokopedia.play.view.layout.video

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.core.view.WindowInsetsCompat
import com.tokopedia.play.view.type.ScreenOrientation

/**
 * Created by jegul on 16/04/20
 */
class PlayVideoLayoutManagerImpl(
        context: Context,
        private val orientation: ScreenOrientation,
        topBounds: Int?,
        @IdRes videoComponentId: Int,
        @IdRes videoLoadingComponentId: Int,
        @IdRes oneTapComponentId: Int,
        @IdRes overlayVideoComponentId: Int
) : PlayVideoLayoutManager {

    private val portraitManager = PlayVideoPortraitManager(
            context = context,
            topBounds = topBounds,
            videoComponentId = videoComponentId,
            videoLoadingComponentId = videoLoadingComponentId,
            oneTapComponentId = oneTapComponentId,
            overlayVideoComponentId = overlayVideoComponentId
    )

    private val landscapeManager = PlayVideoLandscapeManager(
            context = context,
            videoComponentId = videoComponentId,
            videoLoadingComponentId = videoLoadingComponentId,
            oneTapComponentId = oneTapComponentId,
            overlayVideoComponentId = overlayVideoComponentId
    )

    override fun onVideoTopBoundsChanged(view: View, topBounds: Int) {
        getManager().onVideoTopBoundsChanged(view, topBounds)
    }

    override fun layoutView(view: View) {
        getManager().layoutView(view)
    }

    override fun setupInsets(view: View, insets: WindowInsetsCompat) {
        getManager().setupInsets(view, insets)
    }

    override fun onDestroy() {
        getManager().onDestroy()
    }

    private fun getManager(): PlayVideoLayoutManager = when (orientation) {
        ScreenOrientation.Portrait, ScreenOrientation.ReversedPortrait, ScreenOrientation.Unknown -> portraitManager
        ScreenOrientation.Landscape, ScreenOrientation.ReversedLandscape -> landscapeManager
    }
}