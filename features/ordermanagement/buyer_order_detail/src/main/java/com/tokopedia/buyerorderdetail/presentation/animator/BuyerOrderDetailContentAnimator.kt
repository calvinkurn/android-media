package com.tokopedia.buyerorderdetail.presentation.animator

import com.tokopedia.buyerorderdetail.presentation.partialview.BuyerOrderDetailMotionLayout

class BuyerOrderDetailContentAnimator(
    private val buyerOrderDetailMotionLayout: BuyerOrderDetailMotionLayout?
) {

    fun animateToLoadingState() {
        buyerOrderDetailMotionLayout?.run {
            transitionToLoadingState()
        }
    }

    fun animateToShowContent(containsActionButtons: Boolean, onTransitionEnd: () -> Unit) {
        if (containsActionButtons) {
            buyerOrderDetailMotionLayout?.transitionToShowContentWithStickyButton(onTransitionEnd)
        } else {
            buyerOrderDetailMotionLayout?.transitionToShowContentWithoutStickyButton(onTransitionEnd)
        }
    }

    fun animateToErrorState() {
        buyerOrderDetailMotionLayout?.transitionToErrorState()
    }

    fun animateToEmptyStateError() {
        buyerOrderDetailMotionLayout?.transitionToEmptyStateError()
    }
}
