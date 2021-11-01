package com.tokopedia.otp.common.action

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.actionWithAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.pin.PinUnify
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

object PinUnifyAction {

    fun replaceText(text: String): ViewAction {
        return actionWithAssertions(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return allOf(isDisplayed(), ViewMatchers.isAssignableFrom(PinUnify::class.java))
            }

            override fun getDescription(): String {
                return "Replace PinUnify value to $text"
            }

            override fun perform(uiController: UiController?, view: View?) {
                (view as? PinUnify?)?.let {
                    it.value = text
                }
            }
        })
    }
}