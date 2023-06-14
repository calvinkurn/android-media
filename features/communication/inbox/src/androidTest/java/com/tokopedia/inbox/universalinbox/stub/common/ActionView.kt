package com.tokopedia.inbox.universalinbox.stub.common

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher

fun withCustomConstraints(action: ViewAction, constraints: Matcher<View>): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return constraints
        }

        override fun getDescription(): String {
            return action.description
        }

        override fun perform(uiController: UiController?, view: View?) {
            action.perform(uiController, view)
        }
    }
}
