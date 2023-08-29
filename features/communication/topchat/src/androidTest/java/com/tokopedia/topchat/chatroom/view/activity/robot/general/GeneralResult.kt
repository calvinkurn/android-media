package com.tokopedia.topchat.chatroom.view.activity.robot.general

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.hamcrest.core.Is.`is`

object GeneralResult {

    fun assertViewInRecyclerViewAt(
        position: Int,
        viewId: Int,
        matcher: Matcher<View>
    ) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom).atPositionOnView(
                position,
                viewId
            )
        ).check(matches(matcher))
    }

    fun openPageWithApplink(applink: String) {
        intended(hasData(applink))
    }

    fun openPageWithIntent(intent: Intent) {
        intended(hasData(intent.data))
    }

    fun openPageWithExtra(key: String, value: String) {
        intended(hasExtra(key, value))
    }

    fun assertChatRecyclerview(matcher: Matcher<in View>) {
        onView(
            withId(R.id.recycler_view_chatroom)
        ).check(matches(matcher))
    }

    fun <T>assertViewObjectValue(realValue: T, expectedValue: T) {
        assertThat(realValue, `is`(expectedValue))
    }

    fun assertToasterText(msg: String) {
        onView(withText(msg))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    fun assertToasterWithSubText(msg: String) {
        onView(withSubstring(msg)).check(
            matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
        )
    }

    fun assertNoToasterText(msg: String) {
        onView(withText(msg)).check(doesNotExist())
    }

    fun assertKeyboardIsVisible(activity: Activity) {
        val isKeyboardOpened = isKeyboardOpened(activity)
        assertThat(isKeyboardOpened, CoreMatchers.`is`(true))
    }

    fun assertKeyboardIsNotVisible(activity: Activity) {
        val isKeyboardOpened = isKeyboardOpened(activity)
        assertThat(isKeyboardOpened, CoreMatchers.`is`(false))
    }

    private fun isKeyboardOpened(activity: Activity): Boolean {
        val rootView = activity.findViewById<View>(R.id.main)
        return com.tokopedia.topchat.isKeyboardOpened(rootView)
    }
}
