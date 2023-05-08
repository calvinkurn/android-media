package com.tokopedia.profilecompletion.addpin.view.fragment

import android.app.Activity
import android.os.Bundle

class AddPinFrom2FAFragment : AddPinFragment() {

    override fun onSuccessAddPin(isSuccess: Boolean) {
        dismissLoading()
        if (isSuccess) {
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
