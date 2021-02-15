package com.tokopedia.test.application.matcher

import android.content.Intent
import android.content.pm.PackageManager
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher


fun isPointingTo(canonicalName: String): Matcher<Intent> {
    return object : BaseMatcher<Intent>() {
        override fun describeMismatch(item: Any?, description: Description?) {
            super.describeMismatch(item, description)
            if (item is Intent) {
                description?.appendText("\nit's pointing to ${item.component?.className}")
            }
        }

        override fun describeTo(description: Description?) {
            description?.appendText(canonicalName)
        }

        override fun matches(item: Any?): Boolean {
            val intent = item as? Intent ?: return false
            val context = InstrumentationRegistry.getInstrumentation().context
            val resolvedActivities = context.packageManager
                    .queryIntentActivities(intent, PackageManager.MATCH_ALL)
            return !resolvedActivities.none {
                it.activityInfo.packageName == context.packageName &&
                        it.activityInfo.name == canonicalName
            }
        }

    }
}