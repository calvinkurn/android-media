package com.tokopedia.search

import android.app.Activity
import android.content.Intent
import androidx.test.rule.ActivityTestRule

internal open class ActivityTestRuleIntent<T: Activity>(
        private val intent: Intent,
        activityClass: Class<T>?,
        initialTouchMode: Boolean
) : ActivityTestRule<T>(activityClass, initialTouchMode, true) {

    override fun getActivityIntent(): Intent {
        return intent
    }
}