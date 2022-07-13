package com.tokopedia.topchat.chatroom.view.activity.robot.srw

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView
import org.hamcrest.Matchers.not

object SrwResult {

    fun assertSrwLabel(text: String, position: Int) {
        withRecyclerView(R.id.rv_srw_partial)
            .atPositionOnView(position, R.id.label_srw_question)
            .matches(withText(text))
    }

    fun assertSrwLabelVisibility(isVisible: Boolean, position: Int) {
        val matcher = if (isVisible) {
            isDisplayed()
        } else {
            not(isDisplayed())
        }
        withRecyclerView(R.id.rv_srw_partial)
            .atPosition(position)
            .matches(matcher)
    }

    fun assertSrwCoachMark(isVisible: Boolean, text: String) {
        if (isVisible) {
            onView(withSubstring(text))
                .inRoot(isPlatformPopup())
                .check(matches(isDisplayed()))
        } else {
            onView(withSubstring(text))
                .check(doesNotExist())
        }

    }
}