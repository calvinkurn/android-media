package com.tokopedia.otp.verification.view.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.view.View
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.tokopedia.otp.R
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.verification.common.util.SmsBroadcastReceiver
import javax.inject.Inject

class SmsVerificationFragment : VerificationFragment() {

    @Inject
    lateinit var smsBroadcastReceiver: SmsBroadcastReceiver

    @Inject
    lateinit var smsRetrieverClient: SmsRetrieverClient

    override fun initInjector() = getComponent(OtpComponent::class.java).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRetriever()
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

    override fun onResume() {
        super.onResume()
        context?.let {
            smsBroadcastReceiver.register(it, getOtpReceiverListener())
        }
    }

    override fun onPause() {
        super.onPause()
        if (::smsBroadcastReceiver.isInitialized) activity?.unregisterReceiver(smsBroadcastReceiver)
    }

    private fun initRetriever() {
        smsRetrieverClient.startSmsRetriever()
    }

    private fun getOtpReceiverListener(): SmsBroadcastReceiver.ReceiveSMSListener {
        return object : SmsBroadcastReceiver.ReceiveSMSListener {
            override fun onReceiveOTP(otpCode: String) {
                animateText(otpCode)
            }
        }
    }

    companion object {

        fun createInstance(bundle: Bundle?): VerificationFragment {
            val fragment = SmsVerificationFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}