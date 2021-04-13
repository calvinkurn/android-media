package com.tokopedia.profilecompletion.changepin.view.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addpin.view.fragment.PinCompleteFragment
import com.tokopedia.profilecompletion.changepin.view.activity.ChangePinActivity

/**
 * Created by Ade Fulki on 05/01/21.
 */

class ResetPinFragment : ChangePinFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputNewPinState()
        if (activity is ChangePinActivity) (activity as ChangePinActivity).supportActionBar?.title = resources.getString(R.string.change_pin_title_setting)
    }

    override fun handleConfirmState(input: String) {
        if (pin == input) {
            changePinViewModel.resetPin2FA(
                    userId = arguments?.getString(ApplinkConstInternalGlobal.PARAM_USER_ID) ?: "",
                    validateToken = arguments?.getString(ApplinkConstInternalGlobal.PARAM_TOKEN) ?: "")
        } else {
            super.handleConfirmState(input)
        }
    }

    override fun handleInputNewPinState(input: String) {
        changePinViewModel.checkPin2FA(
                pin = input,
                validateToken = arguments?.getString(ApplinkConstInternalGlobal.PARAM_TOKEN) ?: "",
                userId = arguments?.getString(ApplinkConstInternalGlobal.PARAM_USER_ID) ?: "")
    }

    override fun goToSuccessPage() {
        source = PinCompleteFragment.SOURCE_FORGOT_PIN_2FA
        super.goToSuccessPage()
    }

    companion object {

        fun createInstance(bundle: Bundle): ResetPinFragment {
            val fragment = ResetPinFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}