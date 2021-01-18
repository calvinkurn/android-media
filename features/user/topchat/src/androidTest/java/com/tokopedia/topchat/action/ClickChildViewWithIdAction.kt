package com.tokopedia.topchat.action

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.idling.CountingIdlingResource
import org.hamcrest.Matcher

class ClickChildViewWithIdAction(private val idlingResource: CountingIdlingResource?) {
    fun clickChildViewWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController?, view: View) {
                val v: View = view.findViewById(id)
                idlingResource?.increment()
                v.performClick()
            }
        }
    }
}