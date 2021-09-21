package com.tokopedia.profilecompletion.changepin.view.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addpin.view.fragment.PinCompleteFragment
import com.tokopedia.profilecompletion.changepin.view.activity.ChangePinActivity

/**
 * Created by Ade Fulki on 05/01/21.
 */

class ForgotPinFragment : ChangePinFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputNewPinState()
        if (activity is ChangePinActivity) (activity as ChangePinActivity).supportActionBar?.title = resources.getString(R.string.change_pin_title_setting)
    }

    override fun handleInputPin(text: String) {
        if ((text.length == PIN_LENGTH || isValidated) && !isConfirm && !inputNewPin) {
            handleValidatedAndForgotState(text)
        } else {
            super.handleInputPin(text)
        }
    }

    override fun handleConfirmState(input: String) {
        if (pin == input) {
            goToVerificationActivity()
        } else {
            super.handleConfirmState(input)
        }
    }

    override fun goToSuccessPage() {
        source = PinCompleteFragment.SOURCE_FORGOT_PIN
        super.goToSuccessPage()
    }

    companion object {

        fun createInstance(bundle: Bundle): ForgotPinFragment {
            val fragment = ForgotPinFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}