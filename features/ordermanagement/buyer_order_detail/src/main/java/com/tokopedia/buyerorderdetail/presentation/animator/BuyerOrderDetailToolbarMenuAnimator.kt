package com.tokopedia.buyerorderdetail.presentation.animator

import com.tokopedia.buyerorderdetail.presentation.partialview.BuyerOrderDetailToolbarMenu

class BuyerOrderDetailToolbarMenuAnimator(
        private val buyerOrderDetailToolbarMenuMotionLayout: BuyerOrderDetailToolbarMenu
) {

    fun transitionToEmpty() {
        buyerOrderDetailToolbarMenuMotionLayout.transitionToEmpty()
    }

    fun transitionToShowChatIcon() {
        buyerOrderDetailToolbarMenuMotionLayout.transitionToShowChatIcon()
    }
}