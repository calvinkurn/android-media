package com.tokopedia.play.view.layout.video

import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.VideoOrientation

/**
 * Created by jegul on 16/04/20
 */
class PlayVideoLayoutManagerImpl(
        container: ViewGroup,
        orientation: ScreenOrientation,
        videoOrientation: VideoOrientation,
        viewInitializer: PlayVideoViewInitializer
) : PlayVideoLayoutManager {

    private val manager = if (orientation.isLandscape) PlayVideoPortraitManager(
            container = container,
            videoOrientation = videoOrientation,
            viewInitializer = viewInitializer
    ) else PlayVideoLandscapeManager(
            container = container,
            viewInitializer = viewInitializer
    )

    override fun layoutView(view: View) {
        manager.layoutView(view)
    }

    override fun setupInsets(view: View, insets: WindowInsetsCompat) {
        manager.setupInsets(view, insets)
    }

    override fun onDestroy() {
        manager.onDestroy()
    }
}