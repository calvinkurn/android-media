package com.tokopedia.updateinactivephone.stub.features.submitnewphone.regular

import android.os.Bundle
import com.tokopedia.updateinactivephone.features.submitnewphone.regular.InactivePhoneDataUploadFragment

class InactivePhoneDataUploadFragmentStub : InactivePhoneDataUploadFragment() {

    override fun gotoSuccessPage(source: String) {

    }

    companion object {
        fun instance(bundle: Bundle): InactivePhoneDataUploadFragmentStub {
            return InactivePhoneDataUploadFragmentStub().apply {
                arguments = bundle
            }
        }
    }
}