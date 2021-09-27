package com.tokopedia.updateinactivephone.stub.features.successpage.regular

import android.os.Bundle
import com.tokopedia.updateinactivephone.features.successpage.regular.InactivePhoneRegularSuccessFragment
import com.tokopedia.updateinactivephone.features.successpage.withpin.InactivePhoneWithPinSuccessFragment

class InactivePhoneRegularSuccessFragmentStub: InactivePhoneRegularSuccessFragment() {

    override fun gotoHome() {
        //
    }

    companion object {
        fun instance(bundle: Bundle): InactivePhoneRegularSuccessFragmentStub {
            return InactivePhoneRegularSuccessFragmentStub().apply {
                arguments = bundle
            }
        }
    }
}