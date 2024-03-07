package com.tokopedia.verification.stub.verification.view.fragment

import android.os.Bundle
import com.tokopedia.verification.otp.view.fragment.PinVerificationFragment

class PinVerificationFragmentStub : PinVerificationFragment() {

    companion object {
        fun createInstance(
                bundle: Bundle
        ): PinVerificationFragmentStub {
            return PinVerificationFragmentStub().apply {
                arguments = bundle
            }
        }
    }
}
