package com.tokopedia.home.util

import androidx.test.rule.ActivityTestRule

object HomeInstrumentationTestHelper {
    fun ActivityTestRule<*>.deleteHomeDatabase() {
        this.activity.deleteDatabase("HomeCache.db")
    }
}