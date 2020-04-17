package com.tokopedia.play.view.layout.video

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowInsetsCompat
import com.tokopedia.play.R
import com.tokopedia.play.util.changeConstraint

/**
 * Created by jegul on 16/04/20
 */
class PlayVideoPortraitManager(
        context: Context,
        private val topBounds: Int?,
        @IdRes private val videoComponentId: Int,
        @IdRes private val videoLoadingComponentId: Int,
        @IdRes private val oneTapComponentId: Int,
        @IdRes private val overlayVideoComponentId: Int
) : PlayVideoLayoutManager {

    private val offset16 = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)

    override fun onVideoTopBoundsChanged(view: View, topBounds: Int) {
        layoutVideo(container = view, id = videoComponentId, topBounds = topBounds + offset16)
    }

    override fun layoutView(view: View) {
        layoutVideo(container = view, id = videoComponentId, topBounds = if (topBounds != null) topBounds + offset16 else 0)
        layoutVideoLoading(container = view, id = videoLoadingComponentId)
        layoutOneTap(container = view, id = oneTapComponentId)
        layoutOverlayVideo(container = view, id = overlayVideoComponentId)
    }

    override fun setupInsets(view: View, insets: WindowInsetsCompat) {
    }

    override fun onDestroy() {
    }

    private fun layoutVideoBackground(container: View, @IdRes id: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        }
    }

    private fun layoutVideo(container: View, @IdRes id: Int, topBounds: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, topBounds)
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