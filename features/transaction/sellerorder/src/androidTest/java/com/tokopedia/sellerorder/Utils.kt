package com.tokopedia.sellerorder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import com.tokopedia.loginregister.login.view.fragment.LoginEmailPhoneFragment
import com.tokopedia.user.session.UserSession
import org.hamcrest.Matchers

object Utils {

    private const val EMAIL_LOGIN = "try.sugiharto+02@tokopedia.com"
    private const val PASSWORD_LOGIN = "tokopedia789"

    fun login() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val userSession = UserSession(context)
        if (!userSession.isLoggedIn) {
            val activityRuleLogin = ActivityTestRule(LoginActivity::class.java, false, false)
            val bundle = Bundle()
            bundle.putBoolean(LoginEmailPhoneFragment.IS_AUTO_FILL, true)
            bundle.putString(LoginEmailPhoneFragment.AUTO_FILL_EMAIL, EMAIL_LOGIN)
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtras(bundle)
            activityRuleLogin.launchActivity(intent)

            Log.d("SOMListPLTTest", "Checking email...")
            Espresso.onView(ViewMatchers.withId(R.id.register_btn)).perform(ViewActions.click())

            waitForCondition {
                try {
                    getPasswordView().perform(ViewActions.click())
                    Log.d("SOMListPLTTest", "Email registered.")
                    true
                } catch (e: Exception) {
                    false
                }
            }

            getPasswordView().perform(ViewActions.click()).perform(ViewActions.typeText(PASSWORD_LOGIN))

            Log.d("SOMListPLTTest", "Logging in...")
            Espresso.onView(ViewMatchers.withId(R.id.register_btn)).perform(ViewActions.click())

            waitForCondition {
                userSession.isLoggedIn
            }
            Log.d("SOMListPLTTest", "Logged in.")
        }
    }

    private fun waitForCondition(predicate: () -> Boolean) {
        while (!predicate()) {
            InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        }
    }

    private fun getPasswordView(): ViewInteraction {
        return Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.text_field_input), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.wrapper_password))))
    }
}