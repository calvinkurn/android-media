package com.tokopedia.otp.stub.verification.view.fragment

import android.os.Bundle
import androidx.test.espresso.idling.CountingIdlingResource
import com.tokopedia.otp.verification.view.fragment.EmailVerificationFragment

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