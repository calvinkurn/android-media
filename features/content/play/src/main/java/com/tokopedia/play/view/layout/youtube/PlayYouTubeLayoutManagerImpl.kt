package com.tokopedia.play.view.layout.youtube

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowInsetsCompat
import com.tokopedia.play.util.changeConstraint
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.VideoPlayerUiModel

/**
 * Created by jegul on 28/04/20
 */
class PlayYouTubeLayoutManagerImpl(
        container: ViewGroup,
        viewInitializer: PlayYouTubeViewInitializer
) : PlayYouTubeLayoutManager {

    @IdRes private val youTubeComponentId: Int = viewInitializer.onInitYouTube(container)

    override fun layoutView(view: View) {
        layoutYouTube(container = view, id = youTubeComponentId)
    }

    override fun setupInsets(view: View, insets: WindowInsetsCompat) {
    }

    override fun onDestroy() {
    }

    override fun onOrientationChanged(view: View, orientation: ScreenOrientation, videoOrientation: VideoOrientation, videoPlayer: VideoPlayerUiModel) {
    }

    private fun layoutYouTube(container: View, @IdRes id: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        }
    }
}