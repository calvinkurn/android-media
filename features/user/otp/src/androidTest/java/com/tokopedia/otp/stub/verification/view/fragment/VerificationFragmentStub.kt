package com.tokopedia.otp.stub.verification.view.fragment

import android.os.Bundle
import androidx.test.espresso.idling.CountingIdlingResource
import com.tokopedia.otp.verification.view.fragment.VerificationFragment

class VerificationFragmentStub : VerificationFragment() {

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