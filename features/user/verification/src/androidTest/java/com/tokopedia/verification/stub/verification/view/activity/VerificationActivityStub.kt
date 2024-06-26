package com.tokopedia.verification.stub.verification.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.verification.common.di.OtpComponent
import com.tokopedia.verification.stub.common.di.OtpComponentStub
import com.tokopedia.verification.stub.verification.view.fragment.*
import com.tokopedia.verification.otp.base.VerificationTest
import com.tokopedia.verification.otp.data.OtpConstant
import com.tokopedia.verification.otp.data.OtpData
import com.tokopedia.verification.otp.domain.pojo.ModeListData
import com.tokopedia.verification.otp.view.activity.VerificationActivity
import com.tokopedia.verification.otp.view.fragment.VerificationFragment
import com.tokopedia.remoteconfig.RemoteConfigInstance

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
        disableDefaultOtp()
        val fragment = VerificationMethodFragmentStub.createInstance(createBundle())
        doFragmentTransaction(fragment, TAG_OTP_MODE, true)
    }

    fun disableDefaultOtp() {
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
            OtpConstant.KEY_DEFAULT_OTP_ROLLENCE,
            ""
        )
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
            else -> {
                VerificationFragmentStub.createInstance(bundle)
            }
        }
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
