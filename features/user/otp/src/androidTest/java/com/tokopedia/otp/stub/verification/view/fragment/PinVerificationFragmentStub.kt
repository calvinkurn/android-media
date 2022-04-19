package com.tokopedia.otp.stub.verification.view.fragment

import android.os.Bundle
import androidx.test.espresso.idling.CountingIdlingResource
import com.tokopedia.otp.verification.view.fragment.PinVerificationFragment

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