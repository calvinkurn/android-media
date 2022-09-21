package com.tokopedia.feedcomponent.util.coachmark

import android.view.View

/**
 * Created By : Jonathan Darwin on September 21, 2022
 */
class CoachMarkConfig(
    val view: View,
){
    var title: String = ""
        private set
    var subtitle: String = ""
        private set
    var delay: Long = 0L
        private set
    var duration: Long = 0L
        private set

    fun setTitle(title: String) = chainable {
        this.title = title
    }

    fun setSubtitle(subtitle: String) = chainable {
        this.subtitle = subtitle
    }

    fun setDelay(delay: Long) = chainable {
        this.delay = delay
    }

    fun setDuration(duration: Long) = chainable {
        this.duration = duration
    }

    private fun CoachMarkConfig.chainable(fn: () -> Unit): CoachMarkConfig {
        fn()
        return this
    }
}
