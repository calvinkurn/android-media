package com.tokopedia.updateinactivephone.stub.features.submitnewphone.withpin

import android.os.Bundle
import com.tokopedia.updateinactivephone.features.submitnewphone.withpin.InactivePhoneSubmitNewPhoneFragment

class InactivePhoneSubmitNewPhoneFragmentStub : InactivePhoneSubmitNewPhoneFragment() {

    override fun gotoValidateNewPhoneNumber() {

    }

    override fun gotoSuccessPage(source: String) {

    }

    override fun showLoading() {
        // not triggering loader for instrumented test
    }

    override fun hideLoading() {
        // not triggering loader for instrumented test
    }

    companion object {
        fun instance(bundle: Bundle): InactivePhoneSubmitNewPhoneFragmentStub {
            return InactivePhoneSubmitNewPhoneFragmentStub().apply {
                arguments = bundle
            }
        }
    }
}