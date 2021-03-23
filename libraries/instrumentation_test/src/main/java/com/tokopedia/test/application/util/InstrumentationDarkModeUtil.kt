package com.tokopedia.test.application.util

import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.test.application.environment.InstrumentationTestApp

/**
 * Created by Yehezkiel on 11/01/21
 */
const val DARK_MODE_TEST_KEY = "darkMode"

fun isDarkMode(): Boolean {
    val arguments = InstrumentationRegistry.getArguments()
    val isDarkMode = arguments.getString(DARK_MODE_TEST_KEY) ?: ""

    return isDarkMode.toBoolean()
}

fun setupDarkModeTest(forceDarkMode: Boolean = false) {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val application = context.applicationContext as InstrumentationTestApp

    application.setDarkMode(forceDarkMode)
}