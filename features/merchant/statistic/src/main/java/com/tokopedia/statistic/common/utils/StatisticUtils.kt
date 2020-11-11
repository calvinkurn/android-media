package com.tokopedia.statistic.common.utils

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle

/**
 * Created By @ilhamsuaib on 11/11/20
 */

object StatisticUtils {

    fun isActivityResumed(activity: Activity?): Boolean {
        val state = (activity as? AppCompatActivity)?.lifecycle?.currentState
        return state == Lifecycle.State.STARTED || state == Lifecycle.State.RESUMED
    }
}