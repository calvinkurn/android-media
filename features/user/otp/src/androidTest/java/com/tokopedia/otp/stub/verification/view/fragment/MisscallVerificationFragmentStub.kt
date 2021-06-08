package com.tokopedia.otp.stub.verification.view.fragment

import android.os.Bundle
import androidx.test.espresso.idling.CountingIdlingResource
import com.tokopedia.otp.verification.view.fragment.MisscallVerificationFragment

class MisscallVerificationFragmentStub : MisscallVerificationFragment() {

    lateinit var keyboardStateIdling: CountingIdlingResource

    companion object {
        fun createInstance(
                bundle: Bundle,
                keyboardStateIdling: CountingIdlingResource
        ): MisscallVerificationFragmentStub {
            return MisscallVerificationFragmentStub().apply {
                arguments = bundle
                this.keyboardStateIdling = keyboardStateIdling
            }
        }
    }
}