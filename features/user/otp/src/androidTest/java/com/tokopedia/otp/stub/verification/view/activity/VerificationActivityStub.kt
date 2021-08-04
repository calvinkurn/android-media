package com.tokopedia.otp.stub.verification.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.stub.common.di.OtpComponentStub
import com.tokopedia.otp.stub.verification.view.fragment.*
import com.tokopedia.otp.verification.base.VerificationTest
import com.tokopedia.otp.verification.data.OtpData
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.domain.pojo.ModeListData
import com.tokopedia.otp.verification.view.activity.VerificationActivity
import com.tokopedia.otp.verification.view.fragment.VerificationFragment

class VerificationActivityStub : VerificationActivity() {

    lateinit var otpComponentStub: OtpComponentStub

    override fun getTagFragment(): String = TAG

    override fun inflateFragment() { }

    override fun initializeOtpComponent(): OtpComponent = VerificationTest.otpComponent

    fun setupTestFragment(
            otpComponentStub: OtpComponentStub
    ) {
        this.otpComponentStub = otpComponentStub
        doFragmentTransaction(newFragment, TAG, true)
    }

    override fun goToVerificationMethodPage() {
        val fragment = VerificationMethodFragmentStub.createInstance(createBundle())
        doFragmentTransaction(fragment, TAG_OTP_MODE, true)
    }

    override fun generateVerificationFragment(modeListData: ModeListData, bundle: Bundle): VerificationFragment {
        return when (modeListData.modeText) {
            OtpConstant.OtpMode.EMAIL -> {
                EmailVerificationFragmentStub.createInstance(bundle)
            }
            OtpConstant.OtpMode.SMS -> {
                SmsVerificationFragmentStub.createInstance(bundle)
            }
            OtpConstant.OtpMode.WA -> {
                WhatsappVerificationFragmentStub.createInstance(bundle)
            }
            OtpConstant.OtpMode.GOOGLE_AUTH -> {
                GoogleAuthVerificationFragmentStub.createInstance(bundle)
            }
            OtpConstant.OtpMode.PIN -> {
                PinVerificationFragmentStub.createInstance(bundle)
            }
            OtpConstant.OtpMode.MISCALL -> {
                MisscallVerificationFragmentStub.createInstance(bundle)
            }
            else -> {
                VerificationFragmentStub.createInstance(bundle)
            }
        }
    }

    override fun goToOnboardingMiscallPage(modeListData: ModeListData) {
        val fragment = OnboardingMiscallFragmentStub.createInstance(createBundle(modeListData))
        doFragmentTransaction(fragment, TAG_OTP_MISCALL, false)
    }

    override fun goToMethodPageResetPin(otpData: OtpData) {
        isResetPin2FA = true
        val bundle = Bundle().apply {
            putParcelable(OtpConstant.OTP_DATA_EXTRA, otpData)
        }
        val fragment = VerificationMethodFragmentStub.createInstance(bundle)
        doFragmentTransaction(fragment, TAG_OTP_VALIDATOR, false)
    }

    override fun createVerificationMethodFragment(bundle: Bundle): Fragment {
        return VerificationMethodFragmentStub.createInstance(bundle)
    }

    companion object {
        val TAG = VerificationActivityStub::class.java.name
    }
}