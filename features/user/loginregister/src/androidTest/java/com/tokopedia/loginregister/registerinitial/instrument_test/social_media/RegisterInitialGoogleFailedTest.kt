package com.tokopedia.loginregister.registerinitial.instrument_test.social_media

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
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
class RegisterInitialGoogleFailedTest {
    val trackerPath = "tracker/user/loginregister/social_media/loginregister_register_google_failed.json"

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val activityRule = ActivityTestRule(RegisterInitialActivityStub::class.java, true, true)

    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val gtmLogDBSource = GtmLogDBSource(context)
    var idlingResource: TkpdIdlingResource? = null

    lateinit var activity: RegisterInitialActivityStub
    lateinit var fragment: RegisterInitialFragmentStub

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
                Pair(RegisterMockResponse.KEY_LOGIN_TOKEN_CHECK, com.tokopedia.loginregister.test.R.raw.register_gmail_failed)
        )
        setupGraphqlMockResponse(RegisterMockResponse(mockResponse))
    }

    private fun setupIdlingResource() {
        idlingResource = TkpdIdlingResourceProvider.provideIdlingResource("REGISTER_INITIAL")
        if (idlingResource != null)
            IdlingRegistry.getInstance().register(idlingResource?.countingIdlingResource)
        else
            throw RuntimeException("No idling resource found")
    }

    @Test
    fun `check_failed_register_tracker`() {
        checkRegisterGoogleFailed()
        ViewMatchers.assertThat(
                getAnalyticsWithQuery(gtmLogDBSource, context, trackerPath),
                hasAllSuccess()
        )
    }

    private fun checkRegisterGoogleFailed() {
        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withId(R.id.socmed_btn))
                .perform(ViewActions.click())

        //sleep in between
        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withText("Google"))
                .perform(ViewActions.click())
    }

    @After
    fun `unregister_and_finish`() {
        Intents.release()
        idlingResource?.let {
            IdlingRegistry.getInstance().unregister(it.countingIdlingResource)
        }
        activityRule.finishActivity()
    }
}