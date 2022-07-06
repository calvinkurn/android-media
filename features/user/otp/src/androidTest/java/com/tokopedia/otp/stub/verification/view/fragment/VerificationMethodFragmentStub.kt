package com.tokopedia.otp.stub.verification.view.fragment

import android.os.Bundle
import com.tokopedia.otp.verification.view.fragment.VerificationMethodFragment

class VerificationMethodFragmentStub : VerificationMethodFragment() {

    override fun onGoToInactivePhoneNumber() {
        analytics.trackClickInactivePhoneNumber(otpData.otpType.toString())
        analytics.trackClickInactivePhoneLink()
        activity?.finish()
    }

    companion object {
        fun createInstance(
                bundle: Bundle
        ): VerificationMethodFragmentStub {
            return VerificationMethodFragmentStub().apply {
                arguments = bundle
            }
        }
    }
}