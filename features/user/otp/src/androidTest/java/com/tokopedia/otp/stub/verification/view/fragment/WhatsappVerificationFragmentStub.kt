package com.tokopedia.otp.stub.verification.view.fragment

import android.os.Bundle
import androidx.test.espresso.idling.CountingIdlingResource
import com.tokopedia.otp.verification.view.fragment.WhatsappVerificationFragment

class WhatsappVerificationFragmentStub : WhatsappVerificationFragment() {

    lateinit var keyboardStateIdling: CountingIdlingResource

    companion object {
        fun createInstance(
                bundle: Bundle,
                keyboardStateIdling: CountingIdlingResource
        ): WhatsappVerificationFragmentStub {
            return WhatsappVerificationFragmentStub().apply {
                arguments = bundle
                this.keyboardStateIdling = keyboardStateIdling
            }
        }
    }
}