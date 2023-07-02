package com.tokopedia.notifcenter.test.robot.general

import android.net.Uri
import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.times
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.notifcenter.R
import com.tokopedia.test.application.matcher.hasTotalItemOf
import org.hamcrest.Matcher

object GeneralResult {
    fun assertRecyclerviewItem(matcher: Matcher<in View>) {
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view))
            .check(ViewAssertions.matches(matcher))
    }

    fun assertIntentData(data: Uri?, intentCount: Int = 1) {
        Intents.intended(IntentMatchers.hasData(data), times(intentCount))
    }

    fun assertItemListSize(size: Int) {
        assertRecyclerviewItem(hasTotalItemOf(size))
    }
}
