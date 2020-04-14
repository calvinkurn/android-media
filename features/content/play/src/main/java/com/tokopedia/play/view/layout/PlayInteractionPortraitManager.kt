package com.tokopedia.play.view.layout

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowInsetsCompat
import com.tokopedia.play.util.changeConstraint

/**
 * Created by jegul on 13/04/20
 */
class PlayInteractionPortraitManager(
        context: Context,
        @IdRes private val sizeContainerComponentId: Int,
        @IdRes private val sendChatComponentId: Int,
        @IdRes private val likeComponentId: Int,
        @IdRes private val pinnedComponentId: Int,
        @IdRes private val chatListComponentId: Int,
        @IdRes private val videoControlComponentId: Int,
        @IdRes private val gradientBackgroundComponentId: Int,
        @IdRes private val toolbarComponentId: Int,
        @IdRes private val statsInfoComponentId: Int,
        @IdRes private val playButtonComponentId: Int,
        @IdRes private val immersiveBoxComponentId: Int,
        @IdRes private val quickReplyComponentId: Int,
        @IdRes private val endLiveInfoComponentId: Int
) : PlayLayoutManager {

    private val offset24 = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl5)
    private val offset16 = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
    private val offset12 = context.resources.getDimensionPixelOffset(com.tokopedia.play.R.dimen.play_offset_12)
    private val offset8 = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)

    override fun layoutView(view: View) {
        layoutSizeContainer(container = view, id = sizeContainerComponentId)
        layoutToolbar(container = view, id = toolbarComponentId, sizeContainerComponentId = sizeContainerComponentId)
        layoutVideoControl(container = view, id = videoControlComponentId, sizeContainerComponentId = sizeContainerComponentId, likeComponentId = likeComponentId)
        layoutLike(container = view, id = likeComponentId, videoControlComponentId = videoControlComponentId, sizeContainerComponentId = sizeContainerComponentId)
        layoutChat(container = view, id = sendChatComponentId, likeComponentId = likeComponentId, sizeContainerComponentId = sizeContainerComponentId, videoControlComponentId = videoControlComponentId)
        layoutChatList(container = view, id = chatListComponentId, quickReplyComponentId = quickReplyComponentId, likeComponentId = likeComponentId, sizeContainerComponentId = sizeContainerComponentId)
        layoutPinned(container = view, id = pinnedComponentId, chatListComponentId = chatListComponentId, likeComponentId = likeComponentId, sizeContainerComponentId = sizeContainerComponentId)
        layoutPlayButton(container = view, id = playButtonComponentId, sizeContainerComponentId = sizeContainerComponentId)
        layoutImmersiveBox(container = view, id = immersiveBoxComponentId, toolbarComponentId = toolbarComponentId, pinnedComponentId = pinnedComponentId)
        layoutQuickReply(container = view, id = quickReplyComponentId, sendChatComponentId = sendChatComponentId, sizeContainerComponentId = sizeContainerComponentId)
        layoutGradientBackground(container = view, id = gradientBackgroundComponentId)
        layoutEndLiveComponent(container = view, id = endLiveInfoComponentId)
        layoutStatsInfo(container = view, id = statsInfoComponentId, sizeContainerComponentId = sizeContainerComponentId, toolbarComponentId = toolbarComponentId)
    }

    override fun setupInsets(view: View, insets: WindowInsetsCompat) {
        val sizeContainerView = view.findViewById<View>(sizeContainerComponentId)
        val sizeContainerMarginLp = sizeContainerView.layoutParams as ViewGroup.MarginLayoutParams
        sizeContainerMarginLp.bottomMargin = offset16 + insets.systemWindowInsetBottom
        sizeContainerMarginLp.topMargin = insets.systemWindowInsetTop
        sizeContainerView.layoutParams = sizeContainerMarginLp

        val endLiveInfoView = view.findViewById<View>(endLiveInfoComponentId)
        endLiveInfoView.setPadding(endLiveInfoView.paddingLeft, endLiveInfoView.paddingTop, endLiveInfoView.paddingRight, offset24 + insets.systemWindowInsetBottom)
    }

    override fun onDestroy() {

    }

    private fun layoutSizeContainer(container: View, @IdRes id: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        }
    }

    private fun layoutChat(container: View, @IdRes id: Int, @IdRes likeComponentId: Int, @IdRes sizeContainerComponentId: Int, @IdRes videoControlComponentId: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, sizeContainerComponentId, ConstraintSet.START, offset16)
            connect(id, ConstraintSet.END, likeComponentId, ConstraintSet.START, offset8)
            connect(id, ConstraintSet.BOTTOM, videoControlComponentId, ConstraintSet.TOP)
        }
    }

    private fun layoutLike(container: View, @IdRes id: Int, @IdRes videoControlComponentId: Int, @IdRes sizeContainerComponentId: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.END, sizeContainerComponentId, ConstraintSet.END, offset16)
            connect(id, ConstraintSet.BOTTOM, videoControlComponentId, ConstraintSet.BOTTOM)
        }
    }

    private fun layoutChatList(container: View, @IdRes id: Int, @IdRes quickReplyComponentId: Int, @IdRes likeComponentId: Int, @IdRes sizeContainerComponentId: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, sizeContainerComponentId, ConstraintSet.START, offset16)
            connect(id, ConstraintSet.END, likeComponentId, ConstraintSet.START, offset8)
            connect(id, ConstraintSet.BOTTOM, quickReplyComponentId, ConstraintSet.TOP, offset8)
        }
    }

    private fun layoutPinned(container: View, @IdRes id: Int, @IdRes chatListComponentId: Int, @IdRes likeComponentId: Int, @IdRes sizeContainerComponentId: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, sizeContainerComponentId, ConstraintSet.START, offset16)
            connect(id, ConstraintSet.END, likeComponentId, ConstraintSet.START, offset8)
            connect(id, ConstraintSet.BOTTOM, chatListComponentId, ConstraintSet.TOP, offset8)
        }
    }

    private fun layoutVideoControl(container: View, @IdRes id: Int, @IdRes sizeContainerComponentId: Int, @IdRes likeComponentId: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, sizeContainerComponentId, ConstraintSet.START, offset16)
            connect(id, ConstraintSet.END, likeComponentId, ConstraintSet.START, offset8)
            connect(id, ConstraintSet.BOTTOM, sizeContainerComponentId, ConstraintSet.BOTTOM)
        }
    }

    private fun layoutGradientBackground(container: View, @IdRes id: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        }
    }

    private fun layoutToolbar(container: View, @IdRes id: Int, @IdRes sizeContainerComponentId: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, sizeContainerComponentId, ConstraintSet.START, offset16)
            connect(id, ConstraintSet.END, sizeContainerComponentId, ConstraintSet.END, offset16)
            connect(id, ConstraintSet.TOP, sizeContainerComponentId, ConstraintSet.TOP, offset16)
        }
    }

    private fun layoutStatsInfo(container: View, @IdRes id: Int, @IdRes sizeContainerComponentId: Int, @IdRes toolbarComponentId: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, sizeContainerComponentId, ConstraintSet.START, offset16)
            connect(id, ConstraintSet.TOP, toolbarComponentId, ConstraintSet.BOTTOM, offset12)
        }
    }

    private fun layoutPlayButton(container: View, @IdRes id: Int, @IdRes sizeContainerComponentId: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, sizeContainerComponentId, ConstraintSet.START)
            connect(id, ConstraintSet.END, sizeContainerComponentId, ConstraintSet.END)
            connect(id, ConstraintSet.TOP, sizeContainerComponentId, ConstraintSet.TOP)
            connect(id, ConstraintSet.BOTTOM, sizeContainerComponentId, ConstraintSet.BOTTOM)
        }
    }

    private fun layoutImmersiveBox(container: View, @IdRes id: Int, toolbarComponentId: Int, @IdRes pinnedComponentId: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.TOP, toolbarComponentId, ConstraintSet.BOTTOM)
            connect(id, ConstraintSet.BOTTOM, pinnedComponentId, ConstraintSet.TOP, offset16)
        }
    }

    private fun layoutQuickReply(container: View, @IdRes id: Int, sendChatComponentId: Int, @IdRes sizeContainerComponentId: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, sizeContainerComponentId, ConstraintSet.START, offset16)
            connect(id, ConstraintSet.END, sizeContainerComponentId, ConstraintSet.END, offset16)
            connect(id, ConstraintSet.BOTTOM, sendChatComponentId, ConstraintSet.TOP, offset8)
        }
    }

    private fun layoutEndLiveComponent(container: View, @IdRes id: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        }
    }


}