package com.tokopedia.content.common.util.coachmark

import android.view.View

/**
 * Created By : Jonathan Darwin on September 21, 2022
 */
class ContentCoachMarkConfig(
    val view: View,
){
    var title: String = ""
    var subtitle: String = ""
    var delay: Long = 0L
    var duration: Long = 0L
    var onClickCloseListener: () -> Unit = {}
    var onClickListener: () -> Unit = {}

    var coachMarkPrefKey: ContentCoachMarkSharedPref.Key = ContentCoachMarkSharedPref.Key.Unknown
        private set
    var coachMarkPrefKeyId: String = ""
        private set

    val hasPrefKey: Boolean
        get() = coachMarkPrefKey != ContentCoachMarkSharedPref.Key.Unknown

    fun setCoachmarkPrefKey(
        coachMarkPrefKey: ContentCoachMarkSharedPref.Key,
        coachMarkPrefKeyId: String = "",
    ) = chainable {
        this.coachMarkPrefKey = coachMarkPrefKey
        this.coachMarkPrefKeyId = coachMarkPrefKeyId
    }

    private fun ContentCoachMarkConfig.chainable(fn: () -> Unit): ContentCoachMarkConfig {
        fn()
        return this
    }
}
