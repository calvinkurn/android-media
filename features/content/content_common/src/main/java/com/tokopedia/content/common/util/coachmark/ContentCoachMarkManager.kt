package com.tokopedia.content.common.util.coachmark

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.createpost.common.di.ActivityContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 21, 2022
 */
class ContentCoachMarkManager @Inject constructor(
    @ActivityContext private val context: Context,
    private val dispatcher: CoroutineDispatchers,
    private val coachMarkSharedPref: ContentCoachMarkSharedPref,
) {

    private val coachMarkMap = mutableMapOf<View, CoachMark2>()
    private val coachMarkConfigMap = mutableMapOf<View, ContentCoachMarkConfig>()
    private val jobMap = mutableMapOf<View, Job>()

    fun showCoachMark(
        config: ContentCoachMarkConfig,
    ) {
        if(config.hasPrefKey) {
            if(coachMarkSharedPref.hasBeenShown(config.coachMarkPrefKey, config.coachMarkPrefKeyId)) return
        }

        if(config.delay == 0L && config.duration == 0L)
            showCoachMarkInternal(config)
        else {
            jobMap[config.view]?.cancel()
            jobMap[config.view] = CoroutineScope(dispatcher.main).launch {
                if(config.delay != 0L) delay(config.delay)

                showCoachMarkInternal(config)

                if(config.duration != 0L) {
                    delay(config.duration)

                    val coachMark = getOrCreateCoachMark(config)
                    if(coachMark.isShowing) {
                        coachMark.dismissCoachMark()
                    }
                }
            }
        }
    }

    fun dismissCoachmark(view: View) {
        coachMarkMap[view]?.dismissCoachMark()
        jobMap[view]?.cancel()
    }

    fun dismissAllCoachMark() {
        coachMarkMap.forEach {
            coachMarkMap[it.key]?.dismissCoachMark()
        }

        jobMap.forEach {
            jobMap[it.key]?.cancel()
        }
    }

    fun hasBeenShown(view: View) {
        val config = coachMarkConfigMap[view]
        if(config != null && config.hasPrefKey) {
            coachMarkSharedPref.setHasBeenShown(config.coachMarkPrefKey, config.coachMarkPrefKeyId)
        }
    }

    private fun showCoachMarkInternal(
        config: ContentCoachMarkConfig,
    ) {
        val coachMark = getOrCreateCoachMark(config)

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
            if(config.hasPrefKey) {
                coachMarkSharedPref.setHasBeenShown(config.coachMarkPrefKey, config.coachMarkPrefKeyId)
            }

            dismissCoachmark(config.view)
            config.onClickCloseListener()
        }

        coachMark.container?.setOnClickListener {
            config.onClickListener()
        }
    }

    private fun getOrCreateCoachMark(config: ContentCoachMarkConfig): CoachMark2 {
        return coachMarkMap[config.view] ?: CoachMark2(context).also {
            coachMarkMap[config.view] = it
            coachMarkConfigMap[config.view] = config
        }
    }
}
