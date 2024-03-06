package com.tokopedia.verification.stub.verification.view.fragment

import android.os.Bundle
import com.tokopedia.verification.verification.view.fragment.GoogleAuthVerificationFragment

class GoogleAuthVerificationFragmentStub : GoogleAuthVerificationFragment() {

    companion object {
        fun createInstance(
                bundle: Bundle
        ): GoogleAuthVerificationFragmentStub {
            return GoogleAuthVerificationFragmentStub().apply {
                arguments = bundle
            }
        }
    }
}
