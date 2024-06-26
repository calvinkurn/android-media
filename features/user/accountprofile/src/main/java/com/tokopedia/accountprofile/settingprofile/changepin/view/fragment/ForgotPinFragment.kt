package com.tokopedia.accountprofile.settingprofile.changepin.view.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.accountprofile.R
import com.tokopedia.accountprofile.settingprofile.addpin.view.fragment.PinCompleteFragment
import com.tokopedia.accountprofile.settingprofile.changepin.view.activity.ChangePinActivity

/**
 * Created by Ade Fulki on 05/01/21.
 */

class ForgotPinFragment : ChangePinFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputNewPinState()
        if (activity is ChangePinActivity) (activity as ChangePinActivity).supportActionBar?.title =
            context?.resources?.getString(R.string.change_pin_title_setting)
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
