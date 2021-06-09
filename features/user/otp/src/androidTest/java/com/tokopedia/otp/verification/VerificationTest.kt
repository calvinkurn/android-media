package com.tokopedia.otp.verification

import android.content.Context
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.config.GlobalConfig
import com.tokopedia.otp.AndroidFileUtil
import com.tokopedia.otp.idling.FragmentTransactionIdle
import com.tokopedia.otp.stub.common.di.OtpComponentStub
import com.tokopedia.otp.stub.verification.domain.usecase.*
import com.tokopedia.otp.stub.verification.view.activity.VerificationActivityStub
import com.tokopedia.otp.verification.domain.data.OtpRequestPojo
import com.tokopedia.otp.verification.domain.data.OtpValidatePojo
import com.tokopedia.otp.verification.domain.pojo.OtpModeListPojo
import com.tokopedia.otp.R
import com.tokopedia.otp.stub.common.di.OtpComponentStubBuilder
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class VerificationTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
            VerificationActivityStub::class.java, false, false
    )

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val applicationContext: Context
        get() = InstrumentationRegistry
                .getInstrumentation().context.applicationContext

    protected val gtmLogDbSource = GtmLogDBSource(context)

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

    protected open lateinit var activity: VerificationActivityStub
    protected open lateinit var fragmentTransactionIdling: FragmentTransactionIdle
    protected open var keyboardStateIdling: CountingIdlingResource = CountingIdlingResource(
            "Verification-Keyboard"
    )

    protected var smsVerificationMethodSuccess = OtpModeListPojo()
    protected var smsVerificationMethodFailed = OtpModeListPojo()
    protected var smsVerificationMethod2FASuccess = OtpModeListPojo()
    protected var smsVerificationMethod2FAFailed = OtpModeListPojo()
    protected var smsValidateVerificationMethodSuccess = OtpValidatePojo()
    protected var smsValidateVerificationMethodFailed = OtpValidatePojo()
    protected var smsValidateVerificationMethod2FASuccess = OtpValidatePojo()
    protected var smsValidateVerificationMethod2FAFailed = OtpValidatePojo()
    protected var sendSmsVerificationMethodSuccess = OtpRequestPojo()
    protected var sendSmsVerificationMethodFailed = OtpRequestPojo()
    protected var sendSmsVerificationMethod2FASuccess = OtpRequestPojo()
    protected var sendSmsVerificationMethod2FAFailed = OtpRequestPojo()

    protected lateinit var otpComponentStub: OtpComponentStub

    @Before
    open fun before() {
        setupResponse()
        otpComponentStub = OtpComponentStubBuilder.getComponent(applicationContext, context)
        otpComponentStub.inject(this)
        setupSuccessResponses()
        IdlingRegistry.getInstance().register(keyboardStateIdling)
        gtmLogDbSource.deleteAll().subscribe()
    }

    @After
    open fun tearDown() {
        IdlingRegistry.getInstance().unregister(keyboardStateIdling)
        activityTestRule.finishActivity()
    }

    protected open fun setupResponse() {
        smsVerificationMethodSuccess = AndroidFileUtil.parse(
                "get_verification_method_success.json",
                OtpModeListPojo::class.java
        )
        smsVerificationMethodFailed = AndroidFileUtil.parse(
                "get_verification_method_failed.json",
                OtpModeListPojo::class.java
        )
        smsVerificationMethod2FASuccess = AndroidFileUtil.parse(
                "get_verification_method_2FA_success.json",
                OtpModeListPojo::class.java
        )
        smsVerificationMethod2FAFailed = AndroidFileUtil.parse(
                "get_verification_method_2FA_failed.json",
                OtpModeListPojo::class.java
        )
        smsValidateVerificationMethodSuccess = AndroidFileUtil.parse(
                "otp_validate_success.json",
                OtpValidatePojo::class.java
        )
        smsValidateVerificationMethodFailed = AndroidFileUtil.parse(
                "otp_validate_failed.json",
                OtpValidatePojo::class.java
        )
        smsValidateVerificationMethod2FASuccess = AndroidFileUtil.parse(
                "otp_validate_2FA_success.json",
                OtpValidatePojo::class.java
        )
        smsValidateVerificationMethod2FAFailed = AndroidFileUtil.parse(
                "otp_validate_2FA_failed.json",
                OtpValidatePojo::class.java
        )
        sendSmsVerificationMethodSuccess = AndroidFileUtil.parse(
                "send_otp_success.json",
                OtpRequestPojo::class.java
        )
        sendSmsVerificationMethodFailed = AndroidFileUtil.parse(
                "send_otp_failed.json",
                OtpRequestPojo::class.java
        )
        sendSmsVerificationMethod2FASuccess = AndroidFileUtil.parse(
                "send_otp_2FA_success.json",
                OtpRequestPojo::class.java
        )
        sendSmsVerificationMethod2FAFailed = AndroidFileUtil.parse(
                "send_otp_2FA_failed.json",
                OtpRequestPojo::class.java
        )
    }

    protected fun setupSuccessResponses() {
        setupSmsVerificationMethod2FAResponse(true)
        setupSmsVerificationMethodResponse(true)
        setupSmsValidateVerificationMethod2FAResponse(true)
        setupSmsValidateVerificationMethodResponse(true)
        setupSendSmsVerificationMethod2FAResponse(true)
        setupSendSmsVerificationMethodResponse(true)
    }

    protected fun setupFailedResponses() {
        setupSmsVerificationMethod2FAResponse(false)
        setupSmsVerificationMethodResponse(false)
        setupSmsValidateVerificationMethod2FAResponse(false)
        setupSmsValidateVerificationMethodResponse(false)
        setupSendSmsVerificationMethod2FAResponse(false)
        setupSendSmsVerificationMethodResponse(false)
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

    protected fun setupSendSmsVerificationMethodResponse(isSuccess: Boolean) {
        sendOtpUseCase.response = if (isSuccess) sendSmsVerificationMethodSuccess else sendSmsVerificationMethodFailed
    }

    protected fun inflateTestFragment() {
        activity.setupTestFragment(otpComponentStub, keyboardStateIdling)
        waitForFragmentResumed()
    }

    protected fun waitForFragmentResumed() {
        IdlingRegistry.getInstance().register(fragmentTransactionIdling)
        onView(withId(R.id.base_view))
                .check(ViewAssertions.matches(isDisplayed()))
        IdlingRegistry.getInstance().unregister(fragmentTransactionIdling)
    }

    protected fun setupVerificationActivity(
            sourcePage: String? = null,
            isSellerApp: Boolean = false,
            intentModifier: (Intent) -> Unit = {}
    ) {
        val intent = Intent().apply {
            putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, "08987803115")
            putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, "ade.fulki@gmail.com")
            putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, 112)
            putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
            putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
            putExtra(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW, true)
            putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, sourcePage)
        }
        intentModifier(intent)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity
        fragmentTransactionIdling = FragmentTransactionIdle(
                activity.supportFragmentManager,
                VerificationActivityStub.TAG
        )
        if (isSellerApp) {
            GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        }
    }
}