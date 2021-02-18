package com.tokopedia.test.application.matcher

import android.content.Intent
import android.content.pm.PackageManager
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

/**
 * @param activityName - full activity canonical class name for example:
 * com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity
 */
fun isPointingTo(activityName: String): Matcher<Intent> {
    return object : BaseMatcher<Intent>() {
        override fun describeMismatch(item: Any?, description: Description?) {
            super.describeMismatch(item, description)
            if (item is Intent) {
                description?.appendText("\nit's pointing to ${item.component?.className}")
            }
        }

        override fun describeTo(description: Description?) {
            description?.appendText(activityName)
        }

        override fun matches(item: Any?): Boolean {
            val intent = item as? Intent ?: return false
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
    return object : BaseMatcher<Intent>() {
        override fun describeTo(description: Description?) {
            description?.appendText("intent has query \"$key\" with value \"$value\"")
        }

        override fun matches(item: Any?): Boolean {
            val intent = item as? Intent ?: return false
            return intent.data?.getQueryParameter(key) == value
        }
    }
}

fun lastPathSegmentEqualTo(lastPathSegment: String): Matcher<Intent> {
    return object : BaseMatcher<Intent>() {
        override fun describeMismatch(item: Any?, description: Description?) {
            if (item is Intent) {
                description?.appendText(item.data?.lastPathSegment)
            }
        }

        override fun describeTo(description: Description?) {
            description?.appendText(lastPathSegment)
        }

        override fun matches(item: Any?): Boolean {
            val intent = item as? Intent ?: return false
            return intent.data?.lastPathSegment == lastPathSegment
        }
    }
}