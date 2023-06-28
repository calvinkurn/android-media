package com.tokopedia.notifcenter.test.robot.general

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.notifcenter.R
import org.hamcrest.Matcher

object GeneralResult {

    fun assertRecyclerviewItem(matcher: Matcher<in View>) {
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view))
            .check(ViewAssertions.matches(matcher))
    }
}
