package com.tokopedia.otp.stub.verification.view.fragment

import android.os.Bundle
import androidx.test.espresso.idling.CountingIdlingResource
import com.tokopedia.otp.verification.view.fragment.OnboardingMiscallFragment

class OnboardingMiscallFragmentStub : OnboardingMiscallFragment() {

    companion object {
        fun createInstance(
                bundle: Bundle
        ): OnboardingMiscallFragmentStub {
            return OnboardingMiscallFragmentStub().apply {
                arguments = bundle
            }
        }
    }
}