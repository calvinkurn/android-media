package com.tokopedia.buyerorderdetail

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
import com.tokopedia.loginregister.login.const.LoginConstants
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import com.tokopedia.user.session.UserSession
import org.hamcrest.Matchers
import java.util.concurrent.TimeUnit

object Utils {

    private const val EMAIL_LOGIN = "try.sugiharto+02@tokopedia.com"
    private const val PASSWORD_LOGIN = "tokopedia789"

    private fun waitForCondition(timeout: Long = TimeUnit.SECONDS.toMillis(5), predicate: () -> Boolean) {
        val start = System.currentTimeMillis()
        while (!predicate() && System.currentTimeMillis() - start <= timeout) {
            InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        }
    }

    private fun getPasswordView(): ViewInteraction {
        return Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.text_field_input), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.wrapper_password))))
    }

    fun login() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val userSession = UserSession(context)
        if (!userSession.isLoggedIn) {
            val activityRuleLogin = ActivityTestRule(LoginActivity::class.java, false, false)
            val bundle = Bundle()
            bundle.putBoolean(LoginConstants.AutoLogin.IS_AUTO_FILL, true)
            bundle.putString(LoginConstants.AutoLogin.AUTO_FILL_EMAIL, EMAIL_LOGIN)
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtras(bundle)
            activityRuleLogin.launchActivity(intent)

            Log.d("BuyerOrderDetailTest", "Checking email...")
            Espresso.onView(ViewMatchers.withId(R.id.register_btn)).perform(ViewActions.click())

            waitForCondition {
                try {
                    getPasswordView().perform(ViewActions.click())
                    Log.d("BuyerOrderDetailTest", "Email registered.")
                    true
                } catch (e: Exception) {
                    false
                }
            }

            getPasswordView().perform(ViewActions.click()).perform(ViewActions.typeText(PASSWORD_LOGIN))

            Log.d("BuyerOrderDetailTest", "Logging in...")
            Espresso.onView(ViewMatchers.withId(R.id.register_btn)).perform(ViewActions.click())

            waitForCondition {
                userSession.isLoggedIn
            }
            Log.d("BuyerOrderDetailTest", "Logged in.")
        }
    }
}