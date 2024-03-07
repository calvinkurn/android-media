package com.tokopedia.verification.stub.verification.view.fragment

import android.os.Bundle
import com.tokopedia.verification.otp.view.fragment.VerificationFragment

class VerificationFragmentStub : VerificationFragment() {

    override fun redirectAfterValidationSuccessful(bundle: Bundle) { }

    override fun finishFragment() { }

    companion object {
        fun createInstance(
                bundle: Bundle
        ): VerificationFragmentStub {
            return VerificationFragmentStub().apply {
                arguments = bundle
            }
        }
    }
}
