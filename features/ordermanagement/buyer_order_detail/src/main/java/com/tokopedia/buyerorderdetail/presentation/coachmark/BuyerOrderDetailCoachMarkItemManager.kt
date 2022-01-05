package com.tokopedia.buyerorderdetail.presentation.coachmark

import android.content.Context
import android.graphics.Rect
import android.view.View
import com.tokopedia.buyerorderdetail.presentation.model.BaseVisitableUiModel
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.ZERO

abstract class BuyerOrderDetailCoachMarkItemManager {
    abstract var uiModel: BaseVisitableUiModel?
    abstract var viewHolderRootView: View?
    abstract var key: String
    abstract var title: String
    abstract var description: String

    abstract fun getAnchorView(): View?

    private fun getVisiblePercent(v: View): Int {
        if (v.isShown) {
            val r = Rect()
            val isVisible = v.getGlobalVisibleRect(r)
            return if (isVisible) {
                0
            } else {
                -1
            }
        }
        return -1
    }

    fun shouldShowCoachMark(): Boolean {
        return getAnchorView()?.let {
            getVisiblePercent(it) == Int.ZERO
        }.orTrue()
    }

    fun shouldHideCoachMark(): Boolean {
        return getAnchorView()?.let {
            getVisiblePercent(it) != Int.ZERO
        }.orTrue()
    }

    fun markAsShowed(context: Context) {
        CoachMarkPreference.setShown(context, key, true)
    }
}