package com.tokopedia.otp.verification.email

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.otp.common.idling_resource.TkpdIdlingResource
import com.tokopedia.otp.verification.common.ViewActionSpannable
import com.tokopedia.otp.verification.email.stub.OTPEmailMockResponse
import com.tokopedia.otp.verification.email.stub.VerificationActivityStub
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.otp.test.R
import com.tokopedia.otp.verification.common.FreshIdlingResourceTestRule
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.endsWith
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class OTPEmailTest {
    val trackerPath = "tracker/user/otp/otp_email_p1.json"

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val activityRule = ActivityTestRule(VerificationActivityStub::class.java, false, false)

    @get:Rule
    val freshIdlingResourceTestRule = FreshIdlingResourceTestRule()

    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val gtmLogDBSource = GtmLogDBSource(context)
    var idlingResource: IdlingResource? = null

    @Before
    fun setup() {
        Intents.init()
        gtmLogDBSource.deleteAll().subscribe()
        setupGraphqlMockResponse(OTPEmailMockResponse())
        setupIdlingResource()
    }

    private fun setupIdlingResource() {
        idlingResource = TkpdIdlingResource.getIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @Test
    fun `check_otp_email_success_tracker`() {
        activityRule.launchActivity(getTestIntent())

        checkClickOnKirimUlang()
        checkClickOnVerficationButton()
        checkClickOnBackPress()

        ViewMatchers.assertThat(
                getAnalyticsWithQuery(gtmLogDBSource, context, trackerPath),
                hasAllSuccess()
        )
    }

    private fun checkClickOnVerficationButton() {
        Thread.sleep(1000)
        Espresso.onView(allOf(ViewMatchers.withId(R.id.pin_text_field), isDescendantOfA(ViewMatchers.withId(R.id.pin))))
                .check(matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.typeText("9999"))
    }

    private fun checkClickOnKirimUlang() {
        Thread.sleep(31000)
        Espresso.onView(ViewMatchers.withText(endsWith("Kirim ulang")))
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
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, "")
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, "kelvinsaputra+28@tokopedia.com")
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, 126) //OTP_TYPE_REGISTER
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, false)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW, true)
        return intent
    }
}