package com.tokopedia.play.view.layout

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

/**
 * Created by jegul on 01/04/20
 */
class PlayInteractionLayoutManager(
        private val container: ViewGroup
) {

    private val offset16 =   container.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
    private val offset12 = container.resources.getDimensionPixelOffset(com.tokopedia.play.R.dimen.play_offset_12)
    private val offset8 = container.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)

    fun layoutView(
            @IdRes sizeContainerComponentId: Int,
            @IdRes sendChatComponentId: Int,
            @IdRes likeComponentId: Int,
            @IdRes pinnedComponentId: Int,
            @IdRes chatListComponentId: Int,
            @IdRes videoControlComponentId: Int,
            @IdRes gradientBackgroundComponentId: Int,
            @IdRes toolbarComponentId: Int,
            @IdRes statsInfoComponentId: Int,
            @IdRes playButtonComponentId: Int,
            @IdRes immersiveBoxComponentId: Int,
            @IdRes quickReplyComponentId: Int,
            @IdRes endLiveInfoComponentId: Int
    ) {
        layoutSizeContainer(id = sizeContainerComponentId)
        layoutToolbar(id = toolbarComponentId, sizeContainerComponentId = sizeContainerComponentId)
        layoutVideoControl(id = videoControlComponentId, sizeContainerComponentId = sizeContainerComponentId, likeComponentId = likeComponentId)
        layoutLike(id = likeComponentId, videoControlComponentId = videoControlComponentId, sizeContainerComponentId = sizeContainerComponentId)
        layoutChat(id = sendChatComponentId, likeComponentId = likeComponentId, sizeContainerComponentId = sizeContainerComponentId, videoControlComponentId = videoControlComponentId)
        layoutChatList(id = chatListComponentId, quickReplyComponentId = quickReplyComponentId, likeComponentId = likeComponentId, sizeContainerComponentId = sizeContainerComponentId)
        layoutPinned(id = pinnedComponentId, chatListComponentId = chatListComponentId, likeComponentId = likeComponentId, sizeContainerComponentId = sizeContainerComponentId)
        layoutPlayButton(id = playButtonComponentId)
        layoutImmersiveBox(id = immersiveBoxComponentId, toolbarComponentId = toolbarComponentId, pinnedComponentId = pinnedComponentId)
        layoutQuickReply(id = quickReplyComponentId, sendChatComponentId = sendChatComponentId, sizeContainerComponentId = sizeContainerComponentId)
        layoutGradientBackground(id = gradientBackgroundComponentId)
        layoutEndLiveComponent(id = endLiveInfoComponentId)
        layoutStatsInfo(id = statsInfoComponentId, sizeContainerComponentId = sizeContainerComponentId, toolbarComponentId = toolbarComponentId)
    }

    private fun layoutSizeContainer(@IdRes id: Int) {
        changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        }
    }

    private fun layoutChat(@IdRes id: Int, @IdRes likeComponentId: Int, @IdRes sizeContainerComponentId: Int, @IdRes videoControlComponentId: Int) {
        changeConstraint {
            connect(id, ConstraintSet.START, sizeContainerComponentId, ConstraintSet.START, offset16)
            connect(id, ConstraintSet.END, likeComponentId, ConstraintSet.START, offset8)
            connect(id, ConstraintSet.BOTTOM, videoControlComponentId, ConstraintSet.TOP)
        }
    }

    private fun layoutLike(@IdRes id: Int, @IdRes videoControlComponentId: Int, @IdRes sizeContainerComponentId: Int) {
        changeConstraint {
            connect(id, ConstraintSet.END, sizeContainerComponentId, ConstraintSet.END, offset16)
            connect(id, ConstraintSet.BOTTOM, videoControlComponentId, ConstraintSet.BOTTOM)
        }
    }

    private fun layoutChatList(@IdRes id: Int, @IdRes quickReplyComponentId: Int, @IdRes likeComponentId: Int, @IdRes sizeContainerComponentId: Int) {
        changeConstraint {
            connect(id, ConstraintSet.START, sizeContainerComponentId, ConstraintSet.START, offset16)
            connect(id, ConstraintSet.END, likeComponentId, ConstraintSet.START, offset8)
            connect(id, ConstraintSet.BOTTOM, quickReplyComponentId, ConstraintSet.TOP, offset8)
        }
    }

    private fun layoutPinned(@IdRes id: Int, @IdRes chatListComponentId: Int, @IdRes likeComponentId: Int, @IdRes sizeContainerComponentId: Int) {
        changeConstraint {
            connect(id, ConstraintSet.START, sizeContainerComponentId, ConstraintSet.START, offset16)
            connect(id, ConstraintSet.END, likeComponentId, ConstraintSet.START, offset8)
            connect(id, ConstraintSet.BOTTOM, chatListComponentId, ConstraintSet.TOP, offset8)
        }
    }

    private fun layoutVideoControl(@IdRes id: Int, @IdRes sizeContainerComponentId: Int, @IdRes likeComponentId: Int) {
        changeConstraint {
            connect(id, ConstraintSet.START, sizeContainerComponentId, ConstraintSet.START, offset16)
            connect(id, ConstraintSet.END, likeComponentId, ConstraintSet.START, offset8)
            connect(id, ConstraintSet.BOTTOM, sizeContainerComponentId, ConstraintSet.BOTTOM)
        }
    }

    private fun layoutGradientBackground(@IdRes id: Int) {
        changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        }
    }

    private fun layoutToolbar(@IdRes id: Int, @IdRes sizeContainerComponentId: Int) {
        changeConstraint {
            connect(id, ConstraintSet.START, sizeContainerComponentId, ConstraintSet.START, offset16)
            connect(id, ConstraintSet.END, sizeContainerComponentId, ConstraintSet.END, offset16)
            connect(id, ConstraintSet.TOP, sizeContainerComponentId, ConstraintSet.TOP, offset16)
        }
    }

    private fun layoutStatsInfo(@IdRes id: Int, @IdRes sizeContainerComponentId: Int, @IdRes toolbarComponentId: Int) {
        changeConstraint {
            connect(id, ConstraintSet.START, sizeContainerComponentId, ConstraintSet.START, offset16)
            connect(id, ConstraintSet.TOP, toolbarComponentId, ConstraintSet.BOTTOM, offset12)
        }
    }

    private fun layoutPlayButton(@IdRes id: Int) {
        changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        }
    }

    private fun layoutImmersiveBox(@IdRes id: Int, toolbarComponentId: Int, @IdRes pinnedComponentId: Int) {
        changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.TOP, toolbarComponentId, ConstraintSet.BOTTOM)
            connect(id, ConstraintSet.BOTTOM, pinnedComponentId, ConstraintSet.TOP, offset16)
        }
    }

    private fun layoutQuickReply(@IdRes id: Int, sendChatComponentId: Int, @IdRes sizeContainerComponentId: Int) {
        changeConstraint {
            connect(id, ConstraintSet.START, sizeContainerComponentId, ConstraintSet.START, offset16)
            connect(id, ConstraintSet.END, sizeContainerComponentId, ConstraintSet.END, offset16)
            connect(id, ConstraintSet.BOTTOM, sendChatComponentId, ConstraintSet.TOP, offset8)
        }
    }

    private fun layoutEndLiveComponent(@IdRes id: Int) {
        changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        }
    }

    private inline fun changeConstraint(transform: ConstraintSet.() -> Unit) {
        val constraintSet = ConstraintSet()

        constraintSet.clone(container as ConstraintLayout)
        constraintSet.transform()
        constraintSet.applyTo(container)
    }
}