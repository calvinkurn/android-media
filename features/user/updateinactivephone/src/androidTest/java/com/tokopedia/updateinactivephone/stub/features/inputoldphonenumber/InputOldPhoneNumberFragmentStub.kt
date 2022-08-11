package com.tokopedia.updateinactivephone.stub.features.inputoldphonenumber

import android.os.Bundle
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.fragment.InputOldPhoneNumberFragment

class InputOldPhoneNumberFragmentStub : InputOldPhoneNumberFragment() {
    companion object {
        fun newInstance(bundle: Bundle) =
            InputOldPhoneNumberFragmentStub().apply {
                arguments = bundle
            }
    }
}