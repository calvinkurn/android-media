package com.tokopedia.promotionstarget

import android.content.Intent
import android.os.Bundle
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
//import com.tokopedia.loginregister.login.view.activity.LoginActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PushNotificationTest {

//    @get:Rule
//    val activityRule = IntentsTestRule(LoginActivity::class.java, false, false)
//
//    private val context = InstrumentationRegistry.getInstrumentation().targetContext
//    val email = "fernanda.panca+qc8@tokopedia.com"

    @Before
    fun setup() {
//        setupIdlingResource()
        launchActivity()
    }

    @Test
    fun performLogin(){
//        val password = "nopassword"
//        Espresso.onView(ViewMatchers.withId(R.id.register_btn))
//                .perform(click())
//
//        Espresso.onView(ViewMatchers.withId(R.id.password))
//                .perform(ViewActions.typeText("nopassword"))
//
//        Espresso.onView(ViewMatchers.withId(R.id.password))
//                .check(matches(withText(password)))
    }

    private fun launchActivity() {

//        val bundle = Bundle()
//        bundle.putBoolean("auto_fill", true)
//        bundle.putString("email", email)
//        val intent = Intent(context, LoginActivity::class.java)
//        intent.putExtras(bundle)
//        activityRule.launchActivity(intent)
    }

}