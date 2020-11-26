package com.tokopedia.loginregister.registerinitial

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.TkpdIdlingResource
import com.tokopedia.loginregister.TkpdIdlingResourceProvider
import com.tokopedia.loginregister.registerinitial.stub.RegisterInitialActivityStub
import com.tokopedia.loginregister.registerinitial.stub.RegisterInitialFragmentStub
import com.tokopedia.loginregister.registerinitial.stub.RegisterMockResponse
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class RegisterInitialInstrumentTest {

    val trackerPath = "tracker/user/loginregister/login_register_p1_registerinitial.json"

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val activityRule = ActivityTestRule(RegisterInitialActivityStub::class.java, true, true)

    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val gtmLogDBSource = GtmLogDBSource(context)
    var idlingResource: TkpdIdlingResource? = null

    lateinit var activity: RegisterInitialActivityStub
    lateinit var fragment: RegisterInitialFragmentStub

    val emailRegistered = "kelvin.saputra+1@tokopedia.com"
    val emailNotRegistered = "kelvin.saputra+qa99@tokopedia.com"
    val phoneNumberRegistered = "085156364695"
    val phoneNumberNotRegistered = "0851563646951"

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        Intents.init()
        Dispatchers.setMain(TestCoroutineDispatcher())
        gtmLogDBSource.deleteAll().subscribe()
        activity = activityRule.activity
        fragment = activity.setupTestFragment() as RegisterInitialFragmentStub
        setupIdlingResource()
    }

    private fun setupIdlingResource() {
        idlingResource = TkpdIdlingResourceProvider.provideIdlingResource("REGISTER_INITIAL")
        if (idlingResource != null)
            IdlingRegistry.getInstance().register(idlingResource?.countingIdlingResource)
        else
            throw RuntimeException("No idling resource found")
    }

    @Test
    fun `checkRegisterPageTracker`() {
        checkClickButtonLoginTop()
        checkClickButtonLoginBottom()

        checkRegisterEmail()
        checkRegisterGoogle()
        checkRegisterFacebook()
        checkRegisterPhoneNumber()
        checkRegisteredPhoneNumber()

        ViewMatchers.assertThat(
                getAnalyticsWithQuery(gtmLogDBSource, context, trackerPath),
                hasAllSuccess()
        )
    }

    private fun checkRegisterEmail() {
        setupGraphqlMockResponse(RegisterMockResponse(com.tokopedia.loginregister.test.R.raw.register_check_email_notexist))
        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withId(R.id.input_email_phone))
                .perform(ViewActions.replaceText(""))
                .perform(ViewActions.typeText(emailNotRegistered))
        Espresso.onView(ViewMatchers.withId(R.id.register_btn))
                .perform(ViewActions.click())
    }

    private fun checkRegisterGoogle() {
        //stop before start
        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withId(R.id.socmed_btn))
                .perform(ViewActions.click())

        //sleep in between
        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withText("Google"))
                .perform(ViewActions.click())
    }

    private fun checkRegisterFacebook() {
        //stop before start
        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withId(R.id.socmed_btn))
                .perform(ViewActions.click())

        //sleep in between
        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withText("Facebook"))
                .perform(ViewActions.click())
    }

    private fun checkRegisterPhoneNumber() {
        setupGraphqlMockResponse(RegisterMockResponse(com.tokopedia.loginregister.test.R.raw.register_check_phone_notexist))
        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withId(R.id.input_email_phone))
                .perform(ViewActions.replaceText(""))
                .perform(ViewActions.typeText(phoneNumberNotRegistered))

        Espresso.onView(ViewMatchers.withId(R.id.register_btn))
                .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withText("Ya, Benar"))
                .inRoot(isDialog())
                .check(matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())
    }

    private fun checkRegisteredPhoneNumber() {
        setupGraphqlMockResponse(RegisterMockResponse(com.tokopedia.loginregister.test.R.raw.register_check_phone))
        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withId(R.id.input_email_phone))
                .perform(ViewActions.replaceText(""))
                .perform(ViewActions.typeText(phoneNumberRegistered))

        Espresso.onView(ViewMatchers.withId(R.id.register_btn))
                .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withText("Ya, Masuk"))
                .inRoot(isDialog())
                .check(matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())
    }

    private fun checkClickButtonLoginTop() {
        Espresso.onView(ViewMatchers.withId(112))
                .perform(ViewActions.click())
    }

    private fun checkClickButtonLoginBottom() {
        Espresso.onView(ViewMatchers.withId(R.id.login_button))
                .perform(ViewActions.click())
    }

    @After
    fun unregisterIdlingResource() {
        Intents.release()
        idlingResource?.let {
            IdlingRegistry.getInstance().unregister(it.countingIdlingResource)
        }
        activityRule.finishActivity()
    }
}