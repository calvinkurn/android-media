package com.tokopedia.product_ar.util

import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import java.util.LinkedList

object ArCoachMarkUtil {

    fun showCoachMark(coachmarkView: CoachMark2,
                      coachMark2Item: ArrayList<CoachMark2Item>) {
        val coachMarkItemQueue = LinkedList(coachMark2Item)

        coachMarkItemQueue.poll()?.also {
            showCoachMark(coachmarkView, it)
        }

        coachmarkView.simpleCloseIcon?.setOnClickListener {
            val dataAfterClick = coachMarkItemQueue.poll()

            dataAfterClick?.let {
                coachmarkView.hideCoachMark()
                showCoachMark(coachmarkView, dataAfterClick)
            } ?: coachmarkView.hideCoachMark()
        }
    }

    private fun showCoachMark(coachMarkView: CoachMark2, coachMark2Item: CoachMark2Item) {
        coachMarkView.showCoachMark(arrayListOf(coachMark2Item))
    }
}