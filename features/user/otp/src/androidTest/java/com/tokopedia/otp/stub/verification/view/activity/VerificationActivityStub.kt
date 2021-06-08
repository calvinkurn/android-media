package com.tokopedia.otp.stub.verification.view.activity

import android.os.Bundle
import androidx.test.espresso.idling.CountingIdlingResource
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.common.di.OtpComponentBuilder
import com.tokopedia.otp.stub.common.UserSessionStub
import com.tokopedia.otp.stub.common.di.OtpComponentStub
import com.tokopedia.otp.stub.common.di.OtpComponentStubBuilder
import com.tokopedia.otp.stub.verification.view.fragment.*
import com.tokopedia.otp.verification.data.OtpData
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.domain.pojo.ModeListData
import com.tokopedia.otp.verification.view.activity.VerificationActivity
import com.tokopedia.otp.verification.view.fragment.*

class VerificationActivityStub : VerificationActivity() {

    lateinit var otpComponentStub: OtpComponentStub
    lateinit var keyboardStateIdling: CountingIdlingResource


    override fun inflateFragment() {
        // Don't inflate fragment immediately
    }

    override fun setupFragment(savedInstance: Bundle?) {
        userSession = UserSessionStub(this)
        setupParams()
    }

    override fun initializeOtpComponent(): OtpComponent =
            OtpComponentStubBuilder.getComponent(application as BaseMainApplication, this).also {
                otpComponentStub = it
            }

    fun setupTestFragment(
            otpComponentStub: OtpComponentStub,
            keyboardStateIdling: CountingIdlingResource
    ) {
        this.otpComponentStub = otpComponentStub
        this.keyboardStateIdling = keyboardStateIdling
        newFragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(parentViewResourceID, it, TAG)
                .commit()
        }
    }

    override fun goToVerificationMethodPage() {
        val fragment = VerificationMethodFragmentStub.createInstance(createBundle(), keyboardStateIdling)
        doFragmentTransaction(fragment, TAG_OTP_MODE, true)
    }

    override fun generateVerificationFragment(modeListData: ModeListData, bundle: Bundle): VerificationFragment {
        return when (modeListData.modeText) {
            OtpConstant.OtpMode.EMAIL -> {
                EmailVerificationFragmentStub.createInstance(bundle, keyboardStateIdling)
            }
            OtpConstant.OtpMode.SMS -> {
                SmsVerificationFragmentStub.createInstance(bundle, keyboardStateIdling)
            }
            OtpConstant.OtpMode.WA -> {
                WhatsappVerificationFragmentStub.createInstance(bundle, keyboardStateIdling)
            }
            OtpConstant.OtpMode.GOOGLE_AUTH -> {
                GoogleAuthVerificationFragmentStub.createInstance(bundle, keyboardStateIdling)
            }
            OtpConstant.OtpMode.PIN -> {
                PinVerificationFragmentStub.createInstance(bundle, keyboardStateIdling)
            }
            OtpConstant.OtpMode.MISCALL -> {
                MisscallVerificationFragmentStub.createInstance(bundle, keyboardStateIdling)
            }
            else -> {
                VerificationFragmentStub.createInstance(bundle, keyboardStateIdling)
            }
        }
    }

    override fun goToOnboardingMiscallPage(modeListData: ModeListData) {
        val fragment = OnboardingMiscallFragmentStub.createInstance(createBundle(modeListData), keyboardStateIdling)
        doFragmentTransaction(fragment, TAG_OTP_MISCALL, false)
    }

    override fun goToMethodPageResetPin(otpData: OtpData) {
        isResetPin2FA = true
        val bundle = Bundle().apply {
            putParcelable(OtpConstant.OTP_DATA_EXTRA, otpData)
        }
        val fragment = VerificationMethodFragmentStub.createInstance(bundle, keyboardStateIdling)
        doFragmentTransaction(fragment, TAG_OTP_VALIDATOR, false)
    }

    override fun getTagFragment(): String {
        return TAG
    }

    companion object {
        const val TAG = "verification-tag"
    }
}