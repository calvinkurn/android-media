package com.tokopedia.tokopedianow.home.presentation.view.coachmark

import android.content.Context
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item

class SwitcherCoachMark(private val context: Context, private val onDismiss: () -> Unit) {

    private var coachMark: CoachMark2? = null
    private var listItem: ArrayList<CoachMark2Item> = arrayListOf()

    fun show() {
        coachMark = CoachMark2(context)
        coachMark?.onDismissListener = {
            onDismiss
        }
        coachMark?.showCoachMark(listItem)
    }

    fun setCoachMarkItems(items: ArrayList<CoachMark2Item>) {
        listItem = items
    }

    fun hide() {
        coachMark?.dismissCoachMark()
    }
}