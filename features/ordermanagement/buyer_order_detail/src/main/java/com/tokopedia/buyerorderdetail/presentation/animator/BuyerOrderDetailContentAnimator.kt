package com.tokopedia.buyerorderdetail.presentation.animator

import com.tokopedia.buyerorderdetail.presentation.partialview.BuyerOrderDetailMotionLayout
import com.tokopedia.buyerorderdetail.presentation.partialview.BuyerOrderDetailToolbarMenu

class BuyerOrderDetailContentAnimator(
        private val buyerOrderDetailMotionLayout: BuyerOrderDetailMotionLayout?
) {
    fun initPage(onTransitionEnd: () -> Unit) {
        buyerOrderDetailMotionLayout?.run {
            postDelayed({
                animateToLoadingState(onTransitionEnd)
            }, 400L)
        }
    }

    fun animateToLoadingState(onTransitionEnd: () -> Unit) {
        buyerOrderDetailMotionLayout?.run {
            setOnTransitionCompleted {
                setOnTransitionCompleted { }
                onTransitionEnd()
            }
            transitionToLoadingState()
        }
    }

    fun animateToShowContent(containsActionButtons: Boolean) {
        if (containsActionButtons) {
            buyerOrderDetailMotionLayout?.transitionToShowContentWithStickyButton()
        } else {
            buyerOrderDetailMotionLayout?.transitionToShowContentWithoutStickyButton()
        }
    }

    fun animateToErrorState() {
        buyerOrderDetailMotionLayout?.transitionToErrorState()
    }

    fun animateToEmptyStateError() {
        buyerOrderDetailMotionLayout?.transitionToEmptyStateError()
    }
}