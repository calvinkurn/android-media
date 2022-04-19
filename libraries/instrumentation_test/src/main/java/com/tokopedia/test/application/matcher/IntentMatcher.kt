package com.tokopedia.test.application.matcher

import android.content.Intent
import android.content.pm.PackageManager
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * @param activityName - full activity canonical class name for example:
 * com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity
 */
fun isPointingTo(activityName: String): Matcher<Intent> {
    return object : TypeSafeMatcher<Intent>() {
        override fun describeMismatchSafely(
                item: Intent?,
                mismatchDescription: Description?
        ) {
            super.describeMismatchSafely(item, mismatchDescription)
            mismatchDescription?.appendText(
                    "\nit's pointing to ${item?.component?.className}"
            )
        }

        override fun describeTo(description: Description?) {
            description?.appendText(activityName)
        }

        override fun matchesSafely(item: Intent?): Boolean {
            val intent = item ?: return false
            val context = InstrumentationRegistry.getInstrumentation().context
            val resolvedActivities = context.packageManager
                    .queryIntentActivities(intent, PackageManager.MATCH_ALL)
            return resolvedActivities.any {
                it.activityInfo.packageName == context.packageName &&
                        it.activityInfo.name == activityName
            }
        }
    }
}

fun hasQueryParameter(key: String, value: String): Matcher<Intent> {
    return object : TypeSafeMatcher<Intent>() {
        override fun describeTo(description: Description?) {
            description?.appendText("intent has query \"$key\" with value \"$value\"")
        }

        override fun matchesSafely(item: Intent?): Boolean {
            return item?.data?.getQueryParameter(key) == value
        }
    }
}

fun lastPathSegmentEqualTo(lastPathSegment: String): Matcher<Intent> {
    return object : TypeSafeMatcher<Intent>() {
        override fun describeTo(description: Description?) {
            description?.appendText(lastPathSegment)
        }

        override fun describeMismatchSafely(item: Intent?, mismatchDescription: Description?) {
            mismatchDescription?.appendText(item?.data?.lastPathSegment)
        }

        override fun matchesSafely(item: Intent?): Boolean {
            return item?.data?.lastPathSegment == lastPathSegment
        }
    }
}