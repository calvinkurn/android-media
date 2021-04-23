package com.tokopedia.loginregister.login.helper

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.loginregister.R


class LoginSocmedTestHelper {
    companion object {
        const val LOGIN_SOCMED_TRACKER = "tracker/user/loginregister/social_media/login_social_media_p1.json"
        fun clickSocmedButton() {
            Espresso.onView(ViewMatchers.withId(R.id.socmed_btn))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                    .perform(ViewActions.click())
            Espresso.onView(ViewMatchers.withId(R.id.socmed_container)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }

        fun clickGoogleLogin(){
            Espresso.onView(ViewMatchers.withText("Google"))
                    .inRoot(RootMatchers.isDialog())
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                    .perform(ViewActions.click())
        }

        fun clickFacebookLogin(){
            Espresso.onView(ViewMatchers.withText("Facebook"))
                    .inRoot(RootMatchers.isDialog())
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                    .perform(ViewActions.click())
        }

    }
}
