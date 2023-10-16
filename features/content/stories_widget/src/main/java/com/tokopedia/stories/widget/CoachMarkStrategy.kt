package com.tokopedia.stories.widget

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

/**
 * Created by kenny.hadisaputra on 04/10/23
 */
interface CoachMarkStrategy {

    fun canShowCoachMark(): Boolean
}

class DefaultCoachMarkStrategy(
    private val lifecycleOwner: LifecycleOwner,
) : CoachMarkStrategy {

    override fun canShowCoachMark(): Boolean {
        return lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
    }
}

class NoCoachMarkStrategy : CoachMarkStrategy {

    override fun canShowCoachMark(): Boolean {
        return false
    }
}

