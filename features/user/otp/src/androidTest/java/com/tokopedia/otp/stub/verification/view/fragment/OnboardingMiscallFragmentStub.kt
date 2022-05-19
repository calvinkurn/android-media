package com.tokopedia.otp.stub.verification.view.fragment

import android.os.Bundle
import com.tokopedia.otp.verification.view.fragment.miscalll.OnboardingMiscallFragment

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