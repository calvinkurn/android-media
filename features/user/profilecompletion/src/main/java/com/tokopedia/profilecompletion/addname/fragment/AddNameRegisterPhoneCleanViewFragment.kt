package com.tokopedia.profilecompletion.addname.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class AddNameRegisterPhoneCleanViewFragment : AddNameRegisterPhoneFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.tokopedia.profilecompletion.R.layout.fragment_add_name_register_clean_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.attachView(this)
        phoneNumber?.let {
            presenter.registerPhoneNumberAndName("", it)
            analytics.trackClickFinishAddNameButton()
        }
    }

    companion object {
        fun createInstance(bundle: Bundle): AddNameRegisterPhoneCleanViewFragment {
            val fragment = AddNameRegisterPhoneCleanViewFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}