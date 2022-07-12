package com.tokopedia.otp.verification.view.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.view.View
import com.tokopedia.otp.R
import com.tokopedia.otp.verification.data.OtpConstant
import com.tokopedia.otp.verification.domain.data.OtpRequestData
import com.tokopedia.otp.verification.view.activity.VerificationActivity

open class WhatsappVerificationFragment : VerificationFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendOtp()
    }

    override fun setFooterText(spannable: Spannable?) {
        context?.let {
            val spannableChild: Spannable
            if (otpData.canUseOtherMethod && isMoreThanOneMethod) {
                val message = it.getString(R.string.validation_resend_or_with_other_method)
                spannableChild = SpannableString(message)
                setResendOtpFooterSpan(message, spannableChild)
                setOtherMethodFooterSpan(message, spannableChild)
            } else {
                val message = it.getString(R.string.validation_resend)
                spannableChild = SpannableString(message)
                setResendOtpFooterSpan(message, spannableChild)
            }
            super.setFooterText(spannableChild)
        }
    }

    override fun onSuccessSendOtp(otpRequestData: OtpRequestData) {
        if (otpRequestData.errorCode == WA_NOT_REGISTERED_ERROR_CODE &&
            modeListData.modeText == OtpConstant.OtpMode.WA &&
            otpRequestData.messageTitle.isNotEmpty() &&
            otpRequestData.messageSubTitle.isNotEmpty() &&
            otpRequestData.messageImgLink.isNotEmpty()
        ) {
            if (!isFirstSendOtp) {
                if (otpData.otpType == OtpConstant.OtpType.REGISTER_PHONE_NUMBER) {
                    analytics.trackFailedClickResendRegisterPhoneOtpButton(otpRequestData.errorMessage)
                }
                analytics.trackResendOtp(otpData, modeListData, false, otpRequestData.errorMessage)
            } else {
                analytics.trackGenerateOtp(
                    otpData,
                    modeListData,
                    false,
                    otpRequestData.errorMessage
                )
            }
            isFirstSendOtp = false

            (activity as VerificationActivity).goToWhatsappNotRegistered(
                otpRequestData.messageTitle,
                otpRequestData.messageSubTitle,
                otpRequestData.messageImgLink
            )
        } else {
            super.onSuccessSendOtp(otpRequestData)
        }
    }

    companion object {

        const val WA_NOT_REGISTERED_ERROR_CODE = "030002"

        fun createInstance(bundle: Bundle?): VerificationFragment {
            val fragment = WhatsappVerificationFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}