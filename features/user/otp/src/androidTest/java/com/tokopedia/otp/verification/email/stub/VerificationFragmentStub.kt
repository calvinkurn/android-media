package com.tokopedia.otp.verification.email.stub

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.otp.verification.view.fragment.VerificationFragment

class VerificationFragmentStub: VerificationFragment() {
    companion object {
        fun createInstance(bundle: Bundle?): Fragment {
            val fragment = VerificationFragmentStub()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}