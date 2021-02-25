package com.tokopedia.otp.verification.phone

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.otp.common.idling_resource.EspressoIdlingResource
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.otp.test.R
import com.tokopedia.otp.verification.common.FreshIdlingResourceTestRule
import com.tokopedia.otp.verification.common.ViewActionSpannable
import com.tokopedia.otp.verification.view.activity.VerificationActivity
import com.tokopedia.otp.verification.view.adapter.VerificationMethodAdapter
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class OTPSecurityQuestionTest {
    val trackerPath = "tracker/user/otp/otp_security_question_p1.json"

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val activityRule = ActivityTestRule(VerificationActivity::class.java, false, false)

    @get:Rule
    val freshIdlingResourceTestRule = FreshIdlingResourceTestRule()

    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val gtmLogDBSource = GtmLogDBSource(context)
    var idlingResource: IdlingResource? = null

    @Before
    fun setup() {
        Intents.init()
        gtmLogDBSource.deleteAll().subscribe()
        setupIdlingResource()
        setupGraphqlMockResponse(OTPMethodPhoneMockResponse())
    }

    private fun setupIdlingResource() {
        idlingResource = EspressoIdlingResource.idlingResource
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @Test
    fun `check_otp_method_phone_success_tracker`() {
        activityRule.launchActivity(getTestIntent())

        checkClickOnSms()
        checkClickOnVerficationButton()
        checkClickOnKirimUlang()
        checkClickOnBackPress()

        ViewMatchers.assertThat(
                getAnalyticsWithQuery(gtmLogDBSource, context, trackerPath),
                hasAllSuccess()
        )
    }

    private fun checkClickOnSms() {
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.method_list)).check(matches(ViewMatchers.isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition<VerificationMethodAdapter.ViewHolder>(1, ViewActions.click()))
    }

    private fun checkClickOnVerficationButton() {
        Thread.sleep(1000)
        Espresso.onView(CoreMatchers.allOf(ViewMatchers.withId(R.id.pin_text_field), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.pin))))
                .check(matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.typeText("9999"))
    }

    private fun checkClickOnKirimUlang() {
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withText(CoreMatchers.endsWith("Kirim ulang")))
                .check(matches(ViewMatchers.isDisplayed()))
                .perform(ViewActionSpannable.clickClickableSpan("Kirim ulang"))
    }

    private fun checkClickOnBackPress() {
        Thread.sleep(1000)
        Espresso.closeSoftKeyboard()
        Espresso.pressBackUnconditionally()
    }

    @After
    fun unregisterIdlingResource() {
        Intents.release()
        idlingResource?.let {
            IdlingRegistry.getInstance().unregister(it)
        }
        activityRule.finishActivity()
    }

    private fun getTestIntent(): Intent {
        val intent = Intent()
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, "09000123")
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, "")
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, 134) //OTP_SECURITY_QUESTION
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW, true)
        return intent
    }
}