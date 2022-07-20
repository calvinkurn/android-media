package com.tokopedia.updateinactivephone.stub.features.successpage.withpin

import android.os.Bundle
import com.tokopedia.updateinactivephone.features.successpage.withpin.InactivePhoneWithPinSuccessFragment

class InactivePhoneWithPinSuccessFragmentStub: InactivePhoneWithPinSuccessFragment() {

    override fun gotoHome() {
        //
    }

    companion object {
        fun instance(bundle: Bundle): InactivePhoneWithPinSuccessFragmentStub {
            return InactivePhoneWithPinSuccessFragmentStub().apply {
                arguments = bundle
            }
        }
    }
}