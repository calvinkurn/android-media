package com.tokopedia.play.view.layout.video

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowInsetsCompat
import com.tokopedia.play.util.changeConstraint

/**
 * Created by jegul on 24/04/20
 */
class PlayVideoLandscapeManager(
        container: ViewGroup,
        viewInitializer: PlayVideoViewInitializer
) : PlayVideoLayoutManager {

    @IdRes private val videoComponentId: Int = viewInitializer.onInitVideo(container)
    @IdRes private val videoLoadingComponentId: Int = viewInitializer.onInitVideoLoading(container)
    @IdRes private val overlayVideoComponentId: Int = viewInitializer.onInitOverlayVideo(container)

    override fun layoutView(view: View) {
        layoutVideo(container = view, id = videoComponentId)
        layoutVideoLoading(container = view, id = videoLoadingComponentId)
        layoutOverlayVideo(container = view, id = overlayVideoComponentId)
    }

    override fun setupInsets(view: View, insets: WindowInsetsCompat) {

    }

    override fun onDestroy() {

    }

    private fun layoutVideo(container: View, @IdRes id: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
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

    private fun layoutOverlayVideo(container: View, @IdRes id: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        }
    }
}