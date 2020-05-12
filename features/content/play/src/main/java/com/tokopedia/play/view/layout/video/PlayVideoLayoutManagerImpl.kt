package com.tokopedia.play.view.layout.video

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowInsetsCompat
import com.tokopedia.play.R
import com.tokopedia.play.util.changeConstraint
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.VideoPlayerUiModel

/**
 * Created by jegul on 24/04/20
 */
class PlayVideoLayoutManagerImpl(
        container: ViewGroup,
        private val videoOrientation: VideoOrientation,
        viewInitializer: PlayVideoViewInitializer
) : PlayVideoLayoutManager {

    @IdRes private val videoComponentId: Int = viewInitializer.onInitVideo(container)
    @IdRes private val videoLoadingComponentId: Int = viewInitializer.onInitVideoLoading(container)
    @IdRes private val oneTapComponentId: Int = viewInitializer.onInitOneTap(container)
    @IdRes private val overlayVideoComponentId: Int = viewInitializer.onInitOverlayVideo(container)

    override fun layoutView(view: View) {
        layoutVideo(container = view, id = videoComponentId)
        layoutVideoLoading(container = view, id = videoLoadingComponentId)
        layoutOneTap(container = view, id = oneTapComponentId)
        layoutOverlayVideo(container = view, id = overlayVideoComponentId)
    }

    override fun setupInsets(view: View, insets: WindowInsetsCompat) {

    }

    override fun onDestroy() {

    }

    override fun onOrientationChanged(view: View, orientation: ScreenOrientation, videoOrientation: VideoOrientation, videoPlayer: VideoPlayerUiModel) {
    }

    private fun layoutVideo(container: View, @IdRes id: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        }
    }

    private fun layoutVideoLoading(container: View, @IdRes id: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        }
    }

    private fun layoutOneTap(container: View, @IdRes id: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.TOP, R.id.gl_one_tap_post, ConstraintSet.BOTTOM)
        }
    }

    private fun layoutOverlayVideo(container: View, @IdRes id: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        }
    }
}