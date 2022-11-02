package com.tokopedia.feedcomponent.util.coachmark

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import kotlinx.coroutines.*

/**
 * Created By : Jonathan Darwin on September 21, 2022
 */
class CoachMarkHelper(
    private val context: Context,
    private val dispatcher: CoroutineDispatchers,
) {
    private val coachMarkMap = mutableMapOf<View, CoachMark2>()
    private val jobMap = mutableMapOf<View, Job>()

    fun showCoachMark(
        config: CoachMarkConfig,
    ) {
        if(config.delay == 0L && config.duration == 0L)
            showCoachMarkInternal(config)
        else {
            jobMap[config.view]?.cancel()
            jobMap[config.view] = CoroutineScope(dispatcher.main).launch {
                if(config.delay != 0L) delay(config.delay)

                showCoachMarkInternal(config)

                if(config.duration != 0L) {
                    delay(config.duration)

                    val coachMark = getOrCreateCoachMark(config.view)
                    if(coachMark.isShowing) {
                        coachMark.dismissCoachMark()
                    }
                }
            }
        }
    }

    fun dismissCoachmark(view: View) {
        coachMarkMap[view]?.dismissCoachMark()
    }

    fun dismissAllCoachMark() {
        coachMarkMap.forEach {
            coachMarkMap[it.key]?.dismissCoachMark()
        }

        jobMap.forEach {
            jobMap[it.key]?.cancel()
        }
    }

    private fun showCoachMarkInternal(
        config: CoachMarkConfig,
    ) {
        val coachMark = getOrCreateCoachMark(config.view)

        coachMark.isDismissed = false
        coachMark.showCoachMark(
            arrayListOf(
                CoachMark2Item(
                    config.view,
                    config.title,
                    config.subtitle
                )
            )
        )

        coachMark.simpleCloseIcon?.setOnClickListener {
            coachMark.dismissCoachMark()
            config.onClickCloseListener()
        }

        coachMark.container?.setOnClickListener {
            config.onClickListener()
        }
    }

    private fun getOrCreateCoachMark(view: View): CoachMark2 {
        return coachMarkMap[view] ?: CoachMark2(context).also {
            coachMarkMap[view] = it
        }
    }
}
