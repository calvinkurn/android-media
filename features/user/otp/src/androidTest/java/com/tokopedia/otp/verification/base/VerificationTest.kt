package com.tokopedia.otp.verification.base

import android.content.Context
import android.content.Intent
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.otp.R
import com.tokopedia.otp.common.action.PinUnifyAction
import com.tokopedia.otp.common.action.SpannableTypographyAction.clickClickableSpan
import com.tokopedia.otp.common.action.TypographyAction
import com.tokopedia.otp.common.idling.FragmentTransactionIdle
import com.tokopedia.otp.common.matcher.RecyclerViewMatcher
import com.tokopedia.otp.common.utility.RootViewInteractionUtil.waitOnView
import com.tokopedia.otp.stub.common.di.OtpComponentStub
import com.tokopedia.otp.stub.common.di.OtpComponentStubBuilder
import com.tokopedia.otp.stub.verification.domain.usecase.*
import com.tokopedia.otp.stub.verification.view.activity.VerificationActivityStub
import com.tokopedia.otp.verification.domain.data.OtpRequestPojo
import com.tokopedia.otp.verification.domain.data.OtpValidatePojo
import com.tokopedia.otp.verification.domain.pojo.OtpModeListPojo
import org.hamcrest.CoreMatchers
import org.hamcrest.core.StringContains.containsString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.lang.Thread.sleep
import javax.inject.Inject
import javax.inject.Named
import kotlin.streams.asSequence

abstract class VerificationTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
            VerificationActivityStub::class.java, false, false
    )

    @Inject
    protected lateinit var getVerificationMethodUseCase2FA: GetVerificationMethodUseCase2FAStub

    @Inject
    protected lateinit var getVerificationMethodUseCase: GetVerificationMethodUseCaseStub

    @Inject
    protected lateinit var otpValidateUseCase2FA: OtpValidateUseCase2FAStub

    @Inject
    protected lateinit var otpValidateUseCase: OtpValidateUseCaseStub

    @Inject
    protected lateinit var sendOtp2FAUseCase: SendOtp2FAUseCaseStub

    @Inject
    protected lateinit var sendOtpUseCase: SendOtpUseCaseStub

    @Inject
    @Named("OtpMethodSuccess")
    protected lateinit var smsVerificationMethodSuccess: OtpModeListPojo

    @Inject
    @Named("OtpMethodFailed")
    protected lateinit var smsVerificationMethodFailed: OtpModeListPojo

    @Inject
    @Named("OtpMethod2FASuccess")
    protected lateinit var smsVerificationMethod2FASuccess: OtpModeListPojo

    @Inject
    @Named("OtpMethod2FAFailed")
    protected lateinit var smsVerificationMethod2FAFailed: OtpModeListPojo

    @Inject
    @Named("OtpValidateSuccess")
    protected lateinit var smsValidateVerificationMethodSuccess: OtpValidatePojo

    @Inject
    @Named("OtpValidateFailed")
    protected lateinit var smsValidateVerificationMethodFailed: OtpValidatePojo

    @Inject
    @Named("OtpValidate2FASuccess")
    protected lateinit var smsValidateVerificationMethod2FASuccess: OtpValidatePojo

    @Inject
    @Named("OtpValidate2FAFailed")
    protected lateinit var smsValidateVerificationMethod2FAFailed: OtpValidatePojo

    @Inject
    @Named("SendOtpSuccess")
    protected lateinit var sendSmsVerificationMethodSuccess: OtpRequestPojo

    @Inject
    @Named("SendOtpFailed")
    protected lateinit var sendSmsVerificationMethodFailed: OtpRequestPojo

    @Inject
    @Named("SendOtp2FASuccess")
    protected lateinit var sendSmsVerificationMethod2FASuccess: OtpRequestPojo

    @Inject
    @Named("SendOtp2FAFailed")
    protected lateinit var sendSmsVerificationMethod2FAFailed: OtpRequestPojo

    protected open lateinit var activity: VerificationActivityStub
    protected open lateinit var fragmentTransactionIdling: FragmentTransactionIdle

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val applicationContext: Context
        get() = InstrumentationRegistry
                .getInstrumentation().context.applicationContext

    protected val gtmLogDbSource = GtmLogDBSource(context)

    @Before
    open fun before() {
        otpComponent = OtpComponentStubBuilder.getComponent(applicationContext, context)
        otpComponent.inject(this)
        gtmLogDbSource.deleteAll().subscribe()
    }

    @After
    open fun tearDown() {
        activityTestRule.finishActivity()
    }

    protected fun setupSmsVerificationMethod2FAResponse(isSuccess: Boolean) {
        getVerificationMethodUseCase2FA.response = if (isSuccess) smsVerificationMethod2FASuccess else smsVerificationMethod2FAFailed
    }

    protected fun setupSmsVerificationMethodResponse(isSuccess: Boolean) {
        getVerificationMethodUseCase.response = if (isSuccess) smsVerificationMethodSuccess else smsVerificationMethodFailed
    }

    protected fun setupSmsValidateVerificationMethod2FAResponse(isSuccess: Boolean) {
        otpValidateUseCase2FA.response = if (isSuccess) smsValidateVerificationMethod2FASuccess else smsValidateVerificationMethod2FAFailed
    }

    protected fun setupSmsValidateVerificationMethodResponse(isSuccess: Boolean) {
        otpValidateUseCase.response = if (isSuccess) smsValidateVerificationMethodSuccess else smsValidateVerificationMethodFailed
    }

    protected fun setupSendSmsVerificationMethod2FAResponse(isSuccess: Boolean) {
        sendOtp2FAUseCase.response = if (isSuccess) sendSmsVerificationMethod2FASuccess else sendSmsVerificationMethod2FAFailed
    }

    protected fun setupSendOtpVerificationMethodResponse(isSuccess: Boolean) {
        sendOtpUseCase.response = if (isSuccess) sendSmsVerificationMethodSuccess else sendSmsVerificationMethodFailed
    }

    fun runTest(test: () -> Unit) {
        launchDefaultFragment()
        test.invoke()
    }

    private fun launchDefaultFragment() {
        setupVerificationActivity {
            it.putExtras(Intent(context, VerificationActivityStub::class.java))
        }
        inflateTestFragment()
    }

    private fun setupVerificationActivity(
            intentModifier: (Intent) -> Unit = {}
    ) {
        val intent = Intent().apply {
            putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, "08987803115")
            putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, "ade.fulki@gmail.com")
            putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, 112)
            putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
            putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
            putExtra(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW, true)
        }

        intentModifier(intent)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity
        fragmentTransactionIdling = FragmentTransactionIdle(
                activity.supportFragmentManager,
                VerificationActivityStub.TAG
        )
    }

    private fun inflateTestFragment() {
        activity.setupTestFragment(otpComponent)
        waitForFragmentResumed()
    }

    private fun waitForFragmentResumed() {
        IdlingRegistry.getInstance().register(fragmentTransactionIdling)
        onView(withId(R.id.base_view))
                .check(matches(isDisplayed()))
        IdlingRegistry.getInstance().unregister(fragmentTransactionIdling)
    }

    protected fun clickOnBackPress() {
        sleep(1000)
        closeSoftKeyboard()
        pressBackUnconditionally()
    }

    protected fun viewLisfOfMethod() {
        waitOnView(withId(R.id.method_list))
                .check(matches(isDisplayed()))
    }

    protected fun clickVerificationMethod(position: Int, text: String? = null) {
        viewLisfOfMethod()
        onView(RecyclerViewMatcher(R.id.method_list).atPositionOnView(position, R.id.method_text)).apply {
            if (!text.isNullOrEmpty()) {
                check(matches(withText(containsString(text))))
            }
            perform(click())
        }
    }

    protected fun clickInactivePhone() {
        onView(withId(R.id.phone_inactive))
                .check(matches(isDisplayed()))
                .perform(TypographyAction.clickText())
    }

    protected fun inputVerificationOtp(length: Long) {
        val otp = generateRandomOtp(length)
        waitOnView(withId(R.id.pin))
                .check(matches(isDisplayed()))
                .perform(PinUnifyAction.replaceText(otp))
    }

    private fun generateRandomOtp(length: Long): String {
        val source = "0123456789"
        return java.util.Random().ints(length, 0, source.length)
                .asSequence()
                .map(source::get)
                .joinToString("")
    }

    protected fun clickResendOtp() {
        sleep(2000)
        waitOnView(withText(CoreMatchers.containsString("Kirim ulang")), 35000, 500)
                .check(matches(isDisplayed()))
                .perform(clickClickableSpan("Kirim ulang"))
        sleep(2000)
    }

    protected fun clickChooseAnotherOtpMethod() {
        sleep(2000)
        waitOnView(withText(CoreMatchers.containsString("gunakan metode verifikasi lain.")), 35000, 500)
                .check(matches(isDisplayed()))
                .perform(clickClickableSpan("gunakan metode verifikasi lain."))
        sleep(2000)
    }

    companion object {
        lateinit var otpComponent: OtpComponentStub
    }
}