package com.tokopedia.buyerorderdetail.presentation.coachmark

import android.content.Context
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.ZERO

class CoachMarkItemsManager {
    var currentCoachMarkPosition = Int.ZERO
    var coachMarkItemManagers = arrayListOf<BuyerOrderDetailCoachMarkItemManager>()

    fun getUnifyCoachMarkItems(): ArrayList<CoachMark2Item> {
        val coachMarkAnchorView = coachMarkItemManagers.getOrNull(
            currentCoachMarkPosition
        )?.getAnchorView()
        return if (coachMarkAnchorView == null) arrayListOf()
        else ArrayList(coachMarkItemManagers.mapIndexed { index, coachMarkItemManager ->
            CoachMark2Item(
                coachMarkAnchorView,
                title = coachMarkItemManager.title,
                description = coachMarkItemManager.description,
                position = index
            )
        })
    }

    fun getCurrentCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
        return coachMarkItemManagers.getOrNull(currentCoachMarkPosition)
    }

    fun shouldHideCoachMarkItem(): Boolean {
        return coachMarkItemManagers.getOrNull(currentCoachMarkPosition)?.shouldHideCoachMark().orTrue()
    }

    fun updateHasShownStatus(context: Context) {
        coachMarkItemManagers.forEachIndexed { index, buyerOrderDetailCoachMarkItemHandler ->
            if (index <= currentCoachMarkPosition) {
                buyerOrderDetailCoachMarkItemHandler.markAsShowed(context)
            }
        }
    }

    fun reset() {
        coachMarkItemManagers.clear()
        currentCoachMarkPosition = Int.ZERO
    }

    fun getCoachMarkPosition() = currentCoachMarkPosition
}