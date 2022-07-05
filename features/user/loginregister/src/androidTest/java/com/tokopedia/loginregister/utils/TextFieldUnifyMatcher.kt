package com.tokopedia.loginregister.utils

import android.view.View

import androidx.test.espresso.matcher.BoundedMatcher
import com.tokopedia.unifycomponents.TextFieldUnify
import org.hamcrest.Description
import org.hamcrest.Matcher

object TextFieldUnifyMatcher {
    fun isEnabled(integerMatcher: Matcher<Int>, booleanMatcher: Matcher<Boolean>): Matcher<View> {
        return object : BoundedMatcher<View, TextFieldUnify>(TextFieldUnify::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText("error text: ")
                booleanMatcher.describeTo(description)
                integerMatcher.describeTo(description)
            }

            override fun matchesSafely(item: TextFieldUnify): Boolean {
                return booleanMatcher.matches(item.isEnabled) &&
                        integerMatcher.matches(item.id)
            }
        }
    }
}