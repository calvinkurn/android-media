package com.tokopedia.profilecompletion.addname.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal

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

    override fun onErrorRegister(throwable: Throwable) {
        super.onErrorRegister(throwable)
        activity?.let {
            it.setResult(Activity.RESULT_CANCELED, Intent().apply {
                putExtras(Bundle().apply {
                    putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SUCCESS_REGISTER, false)
                    putString(ApplinkConstInternalGlobal.PARAM_MESSAGE_BODY, throwable.message)
                })
            })
            it.finish()
        }
    }
}