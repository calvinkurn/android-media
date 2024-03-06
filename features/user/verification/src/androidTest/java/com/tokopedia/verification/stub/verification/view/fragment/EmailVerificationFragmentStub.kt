package com.tokopedia.verification.stub.verification.view.fragment

import android.os.Bundle
import com.tokopedia.verification.verification.view.fragment.EmailVerificationFragment

class EmailVerificationFragmentStub : EmailVerificationFragment() {

    companion object {
        fun createInstance(
                bundle: Bundle
        ): EmailVerificationFragmentStub {
            return EmailVerificationFragmentStub().apply {
                arguments = bundle
            }
        }
    }
}
