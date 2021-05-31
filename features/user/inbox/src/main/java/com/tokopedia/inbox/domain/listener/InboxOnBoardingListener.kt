package com.tokopedia.inbox.domain.listener

import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item

class InboxOnBoardingListener(
        private val onStepCoach: (
                currentIndex: Int,
                coachMarkItem: CoachMark2Item,
                direction: String,
                previousIndex: Int
        ) -> Unit
): CoachMark2.OnStepListener {
    private var previousIndex = 0

    override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
        val direction = currentIndex - previousIndex
        val directionString = if (direction > 0) {
            "lanjut"
        } else {
            "kembali"
        }
        onStepCoach(currentIndex, coachMarkItem, directionString, previousIndex)
        previousIndex = currentIndex
    }
}