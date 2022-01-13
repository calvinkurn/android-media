package com.tokopedia.review.feature.inbox.pending.presentation.coachmark

import android.content.Context
import android.graphics.Rect
import android.view.View
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.CoachMarkUiModel

abstract class CoachMarkItemManager {
    abstract var uiModel: CoachMarkUiModel?
    abstract var viewHolderRootView: View?
    abstract var key: String
    abstract var title: Int
    abstract var description: Int

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

    fun getTitle(): String {
        return getAnchorView()?.context?.getString(title).orEmpty()
    }

    fun getDescription(): String {
        return getAnchorView()?.context?.getString(description).orEmpty()
    }
}