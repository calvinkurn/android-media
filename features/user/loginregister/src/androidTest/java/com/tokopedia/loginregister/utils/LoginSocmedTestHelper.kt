package com.tokopedia.loginregister.utils

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.loginregister.R


class LoginSocmedTestHelper {
    companion object {
        fun clickSocmedButton() {
            Espresso.onView(ViewMatchers.withId(R.id.socmed_btn))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                    .perform(ViewActions.click())
            Espresso.onView(ViewMatchers.withId(R.id.providerName)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }

        fun clickGoogleLogin(){
            Espresso.onView(ViewMatchers.withText("Google"))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                    .perform(ViewActions.click())
        }
    }
}
