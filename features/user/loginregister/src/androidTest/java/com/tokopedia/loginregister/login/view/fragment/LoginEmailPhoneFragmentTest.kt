package com.tokopedia.loginregister.login.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.SearchView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.ActivityResultMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.TkpdIdlingResource
import com.tokopedia.loginregister.TkpdIdlingResourceProvider
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginEmailPhoneFragmentTest {
    @get:Rule
    val activityRule = IntentsTestRule(LoginActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    var idlingResource: TkpdIdlingResource? = null
    val email = "rahul.lohra@tokopedia.com"

    @Before
    fun setup() {
        clearData()
        setupIdlingResource()
        launchActivity()
    }

    @Test
    fun check_login_events_after_email_password_login() {
        val password = "rahul12345"
        Espresso.onView(ViewMatchers.withId(R.id.register_btn))
                .perform(click())

        Espresso.onView(ViewMatchers.withId(R.id.password))
                .perform(ViewActions.typeText("rahul12345"))

        Espresso.onView(ViewMatchers.withId(R.id.password))
                .check(matches(withText(password)))

        //perform login with email & password - // LoginEmailPhoneFragment.onEmailExist
        Espresso.onView(ViewMatchers.withId(R.id.register_btn))
                .perform(click())

        MatcherAssert.assertThat(activityRule.activityResult, ActivityResultMatchers.hasResultCode(Activity.RESULT_OK))

        val loginBranchIOQuery = "tracker/loginregister/login_branch_io.json"
        val loginAppsFlyerQuery = "tracker/loginregister/login_app_flyer.json"

        MatcherAssert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, loginBranchIOQuery), hasAllSuccess())
        MatcherAssert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, loginAppsFlyerQuery), hasAllSuccess())
    }

    private fun clearData() {
        gtmLogDBSource.deleteAll().toBlocking()
    }

    private fun setupIdlingResource() {
        idlingResource = TkpdIdlingResourceProvider.provideIdlingResource("LOGIN")
        IdlingRegistry.getInstance().register(idlingResource!!.countingIdlingResource)
    }

    private fun launchActivity() {

        val bundle = Bundle()
        bundle.putBoolean(LoginEmailPhoneFragment.IS_AUTO_FILL, true)
        bundle.putString(LoginEmailPhoneFragment.AUTO_FILL_EMAIL, email)
        val intent = Intent(context, LoginActivity::class.java)
        intent.putExtras(bundle)
        activityRule.launchActivity(intent)
    }

    private fun setupFragment() {
        val scenario = launchFragmentInContainer<LoginEmailPhoneFragment>()
        scenario.onFragment {
            try {
                val fieldSmartLock = it.javaClass.getDeclaredField("isEnableSmartLock")
                fieldSmartLock.isAccessible = true
                fieldSmartLock.set(it, false)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }
        scenario.moveToState(Lifecycle.State.CREATED)
    }

    @After
    fun unregisterIdlingResource() {
        idlingResource?.let {
            IdlingRegistry.getInstance().unregister(it.countingIdlingResource)
        }
    }



}