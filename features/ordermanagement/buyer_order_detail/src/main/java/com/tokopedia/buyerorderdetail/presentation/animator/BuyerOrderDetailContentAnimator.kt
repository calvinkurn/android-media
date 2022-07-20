package com.tokopedia.buyerorderdetail.presentation.animator

import com.tokopedia.buyerorderdetail.presentation.partialview.BuyerOrderDetailMotionLayout

class BuyerOrderDetailContentAnimator(
        private val buyerOrderDetailMotionLayout: BuyerOrderDetailMotionLayout?
) {

    companion object {
        private const val DELAY_INITIAL_LOADING_STATE = 500L
    }

    fun initPage(onTransitionEnd: () -> Unit) {
        buyerOrderDetailMotionLayout?.run {
            postDelayed({
                animateToLoadingState(onTransitionEnd)
            }, DELAY_INITIAL_LOADING_STATE)
        }
    }

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

    fun animateToErrorState() {
        buyerOrderDetailMotionLayout?.transitionToErrorState()
    }

    fun animateToEmptyStateError() {
        buyerOrderDetailMotionLayout?.transitionToEmptyStateError()
    }
}