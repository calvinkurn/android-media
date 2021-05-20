package com.tokopedia.loginregister.registerinitial.instrument_test.email

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
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
class RegisterInitialSuccessTest {
    val trackerPath = "tracker/user/loginregister/email/loginregister_register_email_success.json"

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val activityRule = ActivityTestRule(RegisterInitialActivityStub::class.java, true, true)

    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val gtmLogDBSource = GtmLogDBSource(context)
    var idlingResource: TkpdIdlingResource? = null

    lateinit var activity: RegisterInitialActivityStub
    lateinit var fragment: RegisterInitialFragmentStub

    private val emailNotRegistered = "kelvin.saputra+qa99@tokopedia.com"

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        Intents.init()
        Dispatchers.setMain(TestCoroutineDispatcher())
        gtmLogDBSource.deleteAll().subscribe()
        activity = activityRule.activity
        fragment = activity.setupTestFragment() as RegisterInitialFragmentStub
        setupIdlingResource()

        val mockResponse = hashMapOf<String, Int>(
                Pair(RegisterMockResponse.KEY_REGISTER_CHECK, com.tokopedia.loginregister.test.R.raw.register_check_email_notexist),
                Pair(RegisterMockResponse.KEY_LOGIN_TOKEN_CHECK, com.tokopedia.loginregister.test.R.raw.register_gmail),
                Pair(RegisterMockResponse.KEY_PROFILE_CHECK, com.tokopedia.loginregister.test.R.raw.get_profile)
        )
        setupGraphqlMockResponse(RegisterMockResponse(mockResponse))

        val testIntent = Intent()
        testIntent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, emailNotRegistered)
        testIntent.putExtra(ApplinkConstInternalGlobal.PARAM_TOKEN, "testToken")
        testIntent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, "testSource")
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, testIntent))
    }

    private fun setupIdlingResource() {
        idlingResource = TkpdIdlingResourceProvider.provideIdlingResource("REGISTER_INITIAL")
        if (idlingResource != null)
            IdlingRegistry.getInstance().register(idlingResource?.countingIdlingResource)
        else
            throw RuntimeException("No idling resource found")
    }

    @Test
    fun `check_register_email_success_tracker`() {
        checkClickButtonLoginTop()

        checkRegisterEmail()

        ViewMatchers.assertThat(
                getAnalyticsWithQuery(gtmLogDBSource, context, trackerPath),
                hasAllSuccess()
        )
    }

    private fun checkClickButtonLoginTop() {
        Espresso.onView(ViewMatchers.withId(112))
                .perform(ViewActions.click())
    }

    private fun checkRegisterEmail() {
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.input_email_phone))
                .perform(ViewActions.replaceText(""))
                .perform(ViewActions.typeText(emailNotRegistered))
        Espresso.onView(ViewMatchers.withId(R.id.register_btn))
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