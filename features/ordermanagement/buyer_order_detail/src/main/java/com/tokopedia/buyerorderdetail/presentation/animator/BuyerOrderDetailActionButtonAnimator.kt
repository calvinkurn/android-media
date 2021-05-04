package com.tokopedia.buyerorderdetail.presentation.animator

import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.buyerorderdetail.R

class BuyerOrderDetailActionButtonAnimator(
        private val containerActionButton: CardView,
        private val containerBuyerOrderDetail: ConstraintLayout
) {
    fun showActionButtons() {
        containerActionButton.apply {
            val constraintSet = ConstraintSet()
            constraintSet.clone(containerBuyerOrderDetail)
            constraintSet.connect(R.id.containerActionButtons, ConstraintSet.BOTTOM, R.id.containerBuyerOrderDetail, ConstraintSet.BOTTOM)
            constraintSet.clear(R.id.containerActionButtons, ConstraintSet.TOP)
            constraintSet.applyTo(containerBuyerOrderDetail)
        }
    }

    fun hideActionButtons() {
        containerActionButton.apply {
            val constraintSet = ConstraintSet()
            constraintSet.clone(containerBuyerOrderDetail)
            constraintSet.connect(R.id.containerActionButtons, ConstraintSet.TOP, R.id.containerBuyerOrderDetail, ConstraintSet.BOTTOM)
            constraintSet.clear(R.id.containerActionButtons, ConstraintSet.BOTTOM)
            constraintSet.applyTo(containerBuyerOrderDetail)
        }
    }
}