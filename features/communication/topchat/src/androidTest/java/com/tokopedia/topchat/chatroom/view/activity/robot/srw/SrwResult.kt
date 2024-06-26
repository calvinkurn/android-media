package com.tokopedia.topchat.chatroom.view.activity.robot.srw

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.composeAreaResult
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.matchers.atPosition
import com.tokopedia.topchat.matchers.hasSrwBubble
import com.tokopedia.topchat.matchers.isExpanded
import com.tokopedia.topchat.matchers.withIndex
import com.tokopedia.topchat.matchers.withRecyclerView
import com.tokopedia.topchat.matchers.withTotalItem
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher

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
            .check(matches(atPosition(position, R.id.label_srw_question, matcher)))
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

    fun assertSrwPreviewContentIsVisible() {
        assertSrwPreviewContentContainerVisibility(isDisplayed())
        composeAreaResult {
            assertTemplateChatVisibility(not(isDisplayed()))
        }
    }

    fun assertSrwPreviewContentContainerVisibility(
        visibilityMatcher: Matcher<in View>
    ) {
        onView(
            CoreMatchers.allOf(
                withId(R.id.rv_srw_content_container),
                ViewMatchers.isDescendantOfA(withId(R.id.cl_attachment_preview))
            )
        ).check(matches(visibilityMatcher))
    }

    fun assertSrwTitle(
        title: String
    ) {
        onView(withId(R.id.tp_srw_partial)).check(
            matches(withText(title))
        )
    }

    fun assertSrwTotalQuestion(
        totalQuestion: Int
    ) {
        onView(withId(R.id.rv_srw_partial))
            .check(matches(withTotalItem(totalQuestion)))
    }

    fun assertSrwPreviewCollapsed() {
        onView(withId(R.id.rv_srw))
            .check(matches(not(isExpanded())))
    }

    fun assertSrwPreviewExpanded() {
        onView(withId(R.id.rv_srw))
            .check(matches(isExpanded()))
    }

    fun assertSrwBubbleDoesNotExist() {
        generalResult {
            assertChatRecyclerview(not(hasSrwBubble()))
        }
    }

    fun assertSrwBubbleExpanded(
        position: Int
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.chat_srw_bubble, isExpanded())
        }
    }

    fun assertSrwBubbleCollapsed(
        position: Int
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.chat_srw_bubble, not(isExpanded()))
        }
    }

    fun assertSrwBubbleContentContainerVisibility(
        position: Int,
        visibilityMatcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.rv_srw_content_container, visibilityMatcher)
        }
    }
}
