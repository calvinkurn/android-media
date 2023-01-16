package com.tokopedia.topchat.chatroom.view.activity.robot.srw

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.atPosition
import com.tokopedia.topchat.matchers.withIndex
import com.tokopedia.topchat.matchers.withRecyclerView
import org.hamcrest.CoreMatchers.not

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
        onView(withIndex(withId(R.id.rv_srw_partial), 0))
            .check(matches(atPosition(position, R.id.label_srw_question,matcher)))
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
