package com.tokopedia.otp.stub.verification.view.fragment

import android.os.Bundle
import androidx.test.espresso.idling.CountingIdlingResource
import com.tokopedia.otp.verification.view.fragment.WhatsappVerificationFragment

class WhatsappVerificationFragmentStub : WhatsappVerificationFragment() {

    companion object {
        fun createInstance(
                bundle: Bundle
        ): WhatsappVerificationFragmentStub {
            return WhatsappVerificationFragmentStub().apply {
                arguments = bundle
            }
        }
    }
}