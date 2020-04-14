package com.tokopedia.play.view.layout

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

/**
 * Created by jegul on 13/04/20
 */
class PlayInteractionLandscapeManager(
        private val container: ViewGroup
) : PlayInteractionLayoutContract {

    private val offset16 =   container.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
    private val offset12 = container.resources.getDimensionPixelOffset(com.tokopedia.play.R.dimen.play_offset_12)
    private val offset8 = container.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)

    override fun layoutView(
            sizeContainerComponentId: Int,
            sendChatComponentId: Int,
            likeComponentId: Int,
            pinnedComponentId: Int,
            chatListComponentId: Int,
            videoControlComponentId: Int,
            gradientBackgroundComponentId: Int,
            toolbarComponentId: Int,
            statsInfoComponentId: Int,
            playButtonComponentId: Int,
            immersiveBoxComponentId: Int,
            quickReplyComponentId: Int,
            endLiveInfoComponentId: Int) {

        layoutSizeContainer(id = sizeContainerComponentId)
        layoutVideoControl(id = videoControlComponentId, sizeContainerComponentId = sizeContainerComponentId, likeComponentId = likeComponentId)
        layoutLike(id = likeComponentId, videoControlComponentId = videoControlComponentId, sizeContainerComponentId = sizeContainerComponentId)
        layoutPlayButton(id = playButtonComponentId, sizeContainerComponentId = sizeContainerComponentId)
    }

    private fun layoutSizeContainer(@IdRes id: Int) {
        changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        }
    }

    private fun layoutPlayButton(@IdRes id: Int, @IdRes sizeContainerComponentId: Int) {
        changeConstraint {
            connect(id, ConstraintSet.START, sizeContainerComponentId, ConstraintSet.START)
            connect(id, ConstraintSet.END, sizeContainerComponentId, ConstraintSet.END)
            connect(id, ConstraintSet.TOP, sizeContainerComponentId, ConstraintSet.TOP)
            connect(id, ConstraintSet.BOTTOM, sizeContainerComponentId, ConstraintSet.BOTTOM)
        }
    }

    private fun layoutVideoControl(@IdRes id: Int, @IdRes sizeContainerComponentId: Int, @IdRes likeComponentId: Int) {
        changeConstraint {
            connect(id, ConstraintSet.START, sizeContainerComponentId, ConstraintSet.START, offset16)
            connect(id, ConstraintSet.END, likeComponentId, ConstraintSet.START, offset8)
            connect(id, ConstraintSet.BOTTOM, sizeContainerComponentId, ConstraintSet.BOTTOM)
        }
    }

    private fun layoutLike(@IdRes id: Int, @IdRes videoControlComponentId: Int, @IdRes sizeContainerComponentId: Int) {
        changeConstraint {
            connect(id, ConstraintSet.END, sizeContainerComponentId, ConstraintSet.END, offset16)
            connect(id, ConstraintSet.BOTTOM, videoControlComponentId, ConstraintSet.BOTTOM)
        }
    }

    private inline fun changeConstraint(transform: ConstraintSet.() -> Unit) {
        val constraintSet = ConstraintSet()

        constraintSet.clone(container as ConstraintLayout)
        constraintSet.transform()
        constraintSet.applyTo(container)
    }
}