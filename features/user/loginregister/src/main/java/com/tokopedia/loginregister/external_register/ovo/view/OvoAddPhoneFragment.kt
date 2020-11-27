package com.tokopedia.loginregister.external_register.ovo.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.loginregister.external_register.base.fragment.BaseAddPhoneEmailFragment
import com.tokopedia.loginregister.external_register.base.listener.BaseAddPhoneListener
import com.tokopedia.loginregister.external_register.base.listener.BaseDialogConnectAccListener

/**
 * Created by Yoris Prayogo on 17/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class OvoAddPhoneFragment : BaseAddPhoneEmailFragment(), BaseAddPhoneListener, BaseDialogConnectAccListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseAddPhoneListener = this
    }

//    fun onHasOvoAccount() {
//        activity?.run {
//            val dialog = BaseDialogConnectAccount(
//                    title = context?.getString(R.string.ovo_connect_account_title) ?: "",
//                    description = context?.getString(R.string.ovo_connect_account_description)
//                            ?: "",
//                    imgDrawable = R.drawable.img_ovo_collaboration,
//                    listener = this@OvoAddPhoneFragment
//            )
//            dialog.show(supportFragmentManager, "")
//        }
//    }

    override fun onDialogPositiveBtnClicked() {
        startActivity(Intent(context!!, OvoFinalPageActivity::class.java))
    }

    override fun onDialogNegativeBtnClicked() {
        startActivity(Intent(context!!, OvoFinalPageActivity::class.java))
    }

    override fun onAddPhoneNextButtonClicked() {
//        onHasOvoAccount()
    }

    override fun onAddPhoneOtherMethodButtonClicked() {
        startActivity(Intent(context!!, OvoAddNameActivity::class.java))
    }

    companion object {
        fun createInstance(bundle: Bundle? = null): Fragment {
            val fragment = OvoAddPhoneFragment()
            bundle?.run {
                return fragment.apply {
                    arguments = this@run
                }
            }
            return fragment
        }
    }

}