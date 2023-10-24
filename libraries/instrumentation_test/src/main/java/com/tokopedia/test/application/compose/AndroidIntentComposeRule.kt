package com.tokopedia.test.application.compose

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule

/**
 * Created By : Jonathan Darwin on August 09, 2023
 */

inline fun <A: ComponentActivity> createAndroidIntentComposeRule(
    intentFactory: (context: Context) -> Intent
): AndroidComposeTestRule<ActivityScenarioRule<A>, A> {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val intent = intentFactory(context)

    return AndroidComposeTestRule(
        activityRule = ActivityScenarioRule(intent),
        activityProvider = { scenarioRule -> scenarioRule.getActivity() }
    )
}

fun <A : ComponentActivity> ActivityScenarioRule<A>.getActivity(): A {
    var activity: A? = null

    scenario.onActivity { activity = it }

    return activity ?: throw IllegalStateException("Activity was not set in the ActivityScenarioRule!")
}
