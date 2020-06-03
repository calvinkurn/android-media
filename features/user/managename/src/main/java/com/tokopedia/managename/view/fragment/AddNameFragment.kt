package com.tokopedia.managename.view.fragment

import android.os.Bundle
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.session.register.registerphonenumber.view.fragment.AddNameRegisterPhoneFragment

/**
 * Created by Yoris Prayogo on 2020-06-03.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class AddNameFragment : AddNameRegisterPhoneFragment() {
    protected fun onContinueClick() {
        KeyboardHandler.DropKeyboard(getActivity(), getView())
        presenter.addName(etName.getText().toString())
    }

    companion object {
        fun newInstance(bundle: Bundle?): AddNameRegisterPhoneFragment {
            val fragment = AddNameFragment()
            fragment.setArguments(bundle)
            return fragment
        }
    }
}
