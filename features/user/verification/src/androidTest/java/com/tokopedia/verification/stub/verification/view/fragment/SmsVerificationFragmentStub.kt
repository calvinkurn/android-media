package com.tokopedia.verification.stub.verification.view.fragment

import android.os.Bundle
import com.tokopedia.verification.verification.view.fragment.SmsVerificationFragment

class SmsVerificationFragmentStub : SmsVerificationFragment() {

    companion object {
        fun createInstance(
                bundle: Bundle
        ): SmsVerificationFragmentStub {
            return SmsVerificationFragmentStub().apply {
                arguments = bundle
            }
        }
    }
}
