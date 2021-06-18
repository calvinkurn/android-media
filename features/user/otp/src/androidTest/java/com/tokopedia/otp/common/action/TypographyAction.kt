package com.tokopedia.otp.common.action

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

object TypographyAction {
    fun clickText(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return allOf(isDisplayed(), isAssignableFrom(Typography::class.java))
            }

            override fun getDescription(): String {
                return "Click Typography"
            }

            override fun perform(uiController: UiController?, view: View?) {
                val typography = view as Typography
                typography.callOnClick()
            }

        }
    }
}