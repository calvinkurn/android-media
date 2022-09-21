package com.tokopedia.feedcomponent.util.coachmark

import android.content.Context
import android.view.View
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import kotlinx.coroutines.*

/**
 * Created By : Jonathan Darwin on September 21, 2022
 */
class CoachMarkHelper(
    private val context: Context,
) {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val coachMarkMap = mutableMapOf<View, CoachMark2>()

    fun showCoachMark(
        config: CoachMarkConfig,
    ) {
        if(config.delay == 0L && config.duration == 0L)
            showCoachMark(config.view, config.title, config.subtitle)
        else {
            job.cancelChildren()
            scope.launch {
                if(config.delay != 0L) delay(config.delay)

                showCoachMark(config.view, config.title, config.subtitle)

                if(config.duration != 0L) delay(config.duration)

                val coachMark = getOrCreateCoachMark(config.view)
                if(coachMark.isShowing) {
                    coachMark.dismissCoachMark()
                }
            }
        }
    }

    fun dismissAllCoachMark() {
        job.cancelChildren()
        coachMarkMap.forEach {
            hideCoachMark(it.key)
        }
    }

    private fun showCoachMark(
        view: View,
        title: String,
        subtitle: String
    ) {
        val coachMark = getOrCreateCoachMark(view)

        coachMark.isDismissed = false
        coachMark.showCoachMark(
            arrayListOf(
                CoachMark2Item(
                    view,
                    title,
                    subtitle
                )
            )
        )
    }

    private fun hideCoachMark(view: View) {
        coachMarkMap[view]?.dismissCoachMark()
    }

    private fun getOrCreateCoachMark(view: View): CoachMark2 {
        return coachMarkMap[view] ?: CoachMark2(context).also {
            coachMarkMap[view] = it
        }
    }
}
