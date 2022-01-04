package com.tokopedia.tokopedianow.home.presentation.view.coachmark

import android.app.Activity
import android.view.View
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokopedianow.R

class SwitcherCoachMark(private val view: View?, private val onDismiss: () -> Unit) {

    private var coachMark: CoachMark2? = null

    private val activity by lazy { view?.context as? Activity }

    fun show() {
        view?.let {
            val coachMarkItems = arrayListOf(
                CoachMark2Item(
                    view,
                    activity?.getString(R.string.tokopedianow_15m_coachmark_title).orEmpty(),
                    activity?.getString(R.string.tokopedianow_15m_coachmark_description).orEmpty(),
                    CoachMark2.POSITION_BOTTOM
                )
            )
            val marginLeft = activity?.resources?.getDimensionPixelSize(
                R.dimen.tokopedianow_switcher_coachmark_left_margin).orZero()

            coachMark = CoachMark2(view.context)
            coachMark?.onDismissListener = onDismiss
            coachMark?.simpleMarginLeft = marginLeft
            coachMark?.showCoachMark(coachMarkItems)
        }
    }

    fun hide() {
        coachMark?.dismissCoachMark()
    }
}