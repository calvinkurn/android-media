package com.tokopedia.verification.stub.verification.view.fragment

import android.os.Bundle
import com.tokopedia.verification.otp.view.fragment.VerificationMethodFragment

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
