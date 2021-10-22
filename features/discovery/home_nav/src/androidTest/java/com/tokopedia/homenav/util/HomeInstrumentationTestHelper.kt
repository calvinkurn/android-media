package com.tokopedia.homenav.util

import androidx.test.rule.ActivityTestRule

object HomeInstrumentationTestHelper {
    fun ActivityTestRule<*>.deleteHomeDatabase() {
        this.activity.deleteDatabase("HomeCache.db")
    }
}