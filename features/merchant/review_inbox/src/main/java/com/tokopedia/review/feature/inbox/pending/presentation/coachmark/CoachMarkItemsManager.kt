package com.tokopedia.review.feature.inbox.pending.presentation.coachmark

import android.content.Context
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.ZERO

class CoachMarkItemsManager {
    var currentCoachMarkPosition = Int.ZERO
    var coachMarkItemManagers = arrayListOf<CoachMarkItemManager>()

    fun getUnifyCoachMarkItems(): ArrayList<CoachMark2Item> {
        val coachMarkAnchorView = coachMarkItemManagers.getOrNull(
            currentCoachMarkPosition
        )?.getAnchorView()
        return if (coachMarkAnchorView == null) arrayListOf()
        else ArrayList(coachMarkItemManagers.mapIndexed { index, coachMarkItemManager ->
            CoachMark2Item(
                coachMarkAnchorView,
                title = coachMarkItemManager.getTitle(),
                description = coachMarkItemManager.getDescription(),
                position = index
            )
        })
    }

    fun getCurrentCoachMarkItemManager(): CoachMarkItemManager? {
        return coachMarkItemManagers.getOrNull(currentCoachMarkPosition)
    }

    fun shouldHideCoachMarkItem(): Boolean {
        return coachMarkItemManagers.getOrNull(currentCoachMarkPosition)?.shouldHideCoachMark().orTrue()
    }

    fun updateHasShownStatus(context: Context) {
        coachMarkItemManagers.forEachIndexed { index, coachMarkItemManager ->
            if (index <= currentCoachMarkPosition) {
                coachMarkItemManager.markAsShowed(context)
            }
        }
    }

    fun reset() {
        coachMarkItemManagers.clear()
        currentCoachMarkPosition = Int.ZERO
    }

    fun getCoachMarkPosition() = currentCoachMarkPosition
}