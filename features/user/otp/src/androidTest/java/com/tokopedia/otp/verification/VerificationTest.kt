package com.tokopedia.otp.verification

import android.content.Context
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.config.GlobalConfig
import com.tokopedia.otp.AndroidFileUtil
import com.tokopedia.otp.R
import com.tokopedia.otp.idling.FragmentTransactionIdle
import com.tokopedia.otp.stub.common.di.OtpComponentStub
import com.tokopedia.otp.stub.common.di.OtpComponentStubBuilder
import com.tokopedia.otp.stub.verification.domain.usecase.*
import com.tokopedia.otp.stub.verification.view.activity.VerificationActivityStub
import com.tokopedia.otp.verification.domain.data.OtpRequestPojo
import com.tokopedia.otp.verification.domain.data.OtpValidatePojo
import com.tokopedia.otp.verification.domain.pojo.ModeListData
import com.tokopedia.otp.verification.domain.pojo.OtpModeListData
import com.tokopedia.otp.verification.domain.pojo.OtpModeListPojo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject
import javax.inject.Named

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
    protected lateinit var otpComponent: OtpComponentStub

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val applicationContext: Context
        get() = InstrumentationRegistry
                .getInstrumentation().context.applicationContext

    protected val gtmLogDbSource = GtmLogDBSource(context)

    @ExperimentalCoroutinesApi
    @Before
    open fun before() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        otpComponent = OtpComponentStubBuilder.getComponent(applicationContext, context)
        otpComponent.inject(this)
        gtmLogDbSource.deleteAll().subscribe()
    }

    @After
    open fun tearDown() {
        activityTestRule.finishActivity()
    }

    fun runTest(test: () -> Unit) {
        launchDefaultFragment()
        test.invoke()
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

    protected fun setupVerificationActivity(
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

    protected fun launchDefaultFragment() {
        setupVerificationActivity {
            it.putExtras(Intent(context, VerificationActivityStub::class.java))
        }
        inflateTestFragment()
    }

    protected fun inflateTestFragment() {
        activity.setupTestFragment(otpComponent)
        waitForFragmentResumed()
    }

    protected fun waitForFragmentResumed() {
        IdlingRegistry.getInstance().register(fragmentTransactionIdling)
        onView(withId(R.id.parent_container))
                .check(ViewAssertions.matches(isDisplayed()))
        IdlingRegistry.getInstance().unregister(fragmentTransactionIdling)
    }
}