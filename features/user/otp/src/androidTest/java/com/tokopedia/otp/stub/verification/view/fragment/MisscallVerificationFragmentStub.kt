package com.tokopedia.otp.stub.verification.view.fragment

import android.os.Bundle
import androidx.test.espresso.idling.CountingIdlingResource
import com.tokopedia.otp.verification.view.fragment.MisscallVerificationFragment

class MisscallVerificationFragmentStub : MisscallVerificationFragment() {

    companion object {
        fun createInstance(
                bundle: Bundle
        ): MisscallVerificationFragmentStub {
            return MisscallVerificationFragmentStub().apply {
                arguments = bundle
            }
        }
    }
}