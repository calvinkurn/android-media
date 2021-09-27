package com.tokopedia.updateinactivephone.stub.features.submitnewphone.withpin

import android.os.Bundle
import com.tokopedia.updateinactivephone.features.submitnewphone.withpin.InactivePhoneSubmitNewPhoneFragment

class InactivePhoneSubmitNewPhoneFragmentStub : InactivePhoneSubmitNewPhoneFragment() {

    override fun gotoPhoneVerification() {

    }

    override fun gotoSuccessPage() {

    }

    companion object {
        fun instance(bundle: Bundle): InactivePhoneSubmitNewPhoneFragmentStub {
            return InactivePhoneSubmitNewPhoneFragmentStub().apply {
                arguments = bundle
            }
        }
    }
}