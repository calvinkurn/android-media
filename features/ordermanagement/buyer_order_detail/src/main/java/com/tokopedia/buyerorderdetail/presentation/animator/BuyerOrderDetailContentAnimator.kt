package com.tokopedia.buyerorderdetail.presentation.animator

import com.tokopedia.buyerorderdetail.presentation.partialview.BuyerOrderDetailMotionLayout

class BuyerOrderDetailContentAnimator(
    private val buyerOrderDetailMotionLayout: BuyerOrderDetailMotionLayout?
) {

    fun animateToLoadingState(onTransitionEnd: () -> Unit) {
        buyerOrderDetailMotionLayout?.run {
            transitionToLoadingState(onTransitionEnd)
        }
    }

    fun animateToShowContent(containsActionButtons: Boolean, onTransitionEnd: () -> Unit) {
        if (containsActionButtons) {
            buyerOrderDetailMotionLayout?.transitionToShowContentWithStickyButton(onTransitionEnd)
        } else {
            buyerOrderDetailMotionLayout?.transitionToShowContentWithoutStickyButton(onTransitionEnd)
        }
    }

    fun animateToErrorState(onTransitionEnd: () -> Unit) {
        buyerOrderDetailMotionLayout?.transitionToErrorState(onTransitionEnd)
    }

    fun animateToEmptyStateError(onTransitionEnd: () -> Unit) {
        buyerOrderDetailMotionLayout?.transitionToEmptyStateError(onTransitionEnd)
    }
}
