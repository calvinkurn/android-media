package com.tokopedia.test.application.util

import android.app.Activity
import android.content.Intent
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule

inline fun <reified T: Activity> ActivityTestRule<T>.launchActivityWithDefaultIntent() {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.setClassName(InstrumentationRegistry.getInstrumentation().targetContext.packageName, T::class.java.name)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    launchActivity(intent)
}