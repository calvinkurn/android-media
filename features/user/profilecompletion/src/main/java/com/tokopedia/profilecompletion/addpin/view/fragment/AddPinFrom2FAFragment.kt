package com.tokopedia.profilecompletion.addpin.view.fragment

import android.app.Activity
import android.os.Bundle
import com.tokopedia.profilecompletion.addpin.data.AddChangePinData

class AddPinFrom2FAFragment : AddPinFragment() {

    override fun onSuccessAddPin(addChangePinData: AddChangePinData) {
        dismissLoading()
        if (addChangePinData.success) {
			trackingPinUtil.trackSuccessInputConfirmationPin()
			activity?.setResult(Activity.RESULT_OK)
			activity?.finish()
        }
    }

    companion object {

        fun createInstance(bundle: Bundle): AddPinFrom2FAFragment {
            val fragment = AddPinFrom2FAFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}