package com.tokopedia.notifcenter.test.robot.general

import android.net.Uri
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.times
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.stub.common.hasGlobalIcon
import com.tokopedia.test.application.matcher.hasTotalItemOf
import org.hamcrest.Matcher

object GeneralResult {
    fun assertRecyclerviewItem(matcher: Matcher<in View>) {
        onView(withId(R.id.recycler_view))
            .check(matches(matcher))
    }

    fun assertIntentData(data: Uri?, intentCount: Int = 1) {
        Thread.sleep(1000) // Wait until the intent called
        Intents.intended(IntentMatchers.hasData(data), times(intentCount))
    }

    fun assertItemListSize(size: Int) {
        assertRecyclerviewItem(hasTotalItemOf(size))
    }

    fun assertNavToolbarGlobal() {
        onView(withId(R.id.notifcenter_nav_toolbar))
            .check(matches(hasGlobalIcon()))
    }
}
