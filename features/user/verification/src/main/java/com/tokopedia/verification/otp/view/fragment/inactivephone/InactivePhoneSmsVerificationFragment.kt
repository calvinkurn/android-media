package com.tokopedia.verification.otp.view.fragment.inactivephone

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.verification.otp.view.fragment.SmsVerificationFragment
import com.tokopedia.verification.otp.view.fragment.VerificationFragment

open class InactivePhoneSmsVerificationFragment : SmsVerificationFragment() {

    override fun sendOtp() {
        if (isCountdownFinished()) {
            viewModel.sendOtp2FA(
                otpType = otpData.otpType.toString(),
                mode = modeListData.modeText,
                msisdn = otpData.msisdn,
                email = otpData.email,
                otpDigit = modeListData.otpDigit,
                validateToken = otpData.accessToken,
                userIdEnc = otpData.userIdEnc
            )
        } else {
            setFooterText()
        }
    }

    override fun validate(code: String) {
        viewModel.otpValidate2FA(
            code = code,
            otpType = otpData.otpType.toString(),
            mode = modeListData.modeText,
            userIdEnc = otpData.userIdEnc,
            validateToken = otpData.accessToken,
            msisdn = otpData.msisdn,
            userId = otpData.userId.toIntOrZero()
        )
    }

    companion object {
        fun createInstance(bundle: Bundle?): VerificationFragment {
            val fragment = InactivePhoneSmsVerificationFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}
