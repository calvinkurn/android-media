package com.tokopedia.home.util

import android.content.Context
import androidx.test.rule.ActivityTestRule

object HomeInstrumentationTestHelper {
    fun ActivityTestRule<*>.deleteHomeDatabase() {
        this.activity.deleteDatabase("HomeCache.db")
    }

    fun Context.deleteHomeDatabase() {
        this.deleteDatabase("HomeCache.db")
    }
}