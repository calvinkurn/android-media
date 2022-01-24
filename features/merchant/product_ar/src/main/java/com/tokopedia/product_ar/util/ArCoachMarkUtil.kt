package com.tokopedia.product_ar.util

import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import java.util.Stack

object ArCoachMarkUtil {

    fun showCoachMark(coachmarkView: CoachMark2,
                      coachMark2Item: ArrayList<CoachMark2Item>) {
        val stack = Stack<CoachMark2Item>()
        stack.addAll(coachMark2Item.reversed())

        val data = stack.pop()
        showCoachMark(coachmarkView, data)

        coachmarkView.simpleCloseIcon?.setOnClickListener {
            if (stack.size == 0) {
                coachmarkView.hideCoachMark()
            } else {
                val dataAfterClick = stack.pop()
                coachmarkView.hideCoachMark()
                showCoachMark(coachmarkView, dataAfterClick)
            }
        }
    }

    private fun showCoachMark(coachMarkView: CoachMark2, coachMark2Item: CoachMark2Item) {
        coachMarkView.showCoachMark(arrayListOf(coachMark2Item))
    }
}