package com.tokopedia.verification.stub.verification.view.fragment

import android.os.Bundle
import com.tokopedia.verification.otp.view.fragment.WhatsappVerificationFragment

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
