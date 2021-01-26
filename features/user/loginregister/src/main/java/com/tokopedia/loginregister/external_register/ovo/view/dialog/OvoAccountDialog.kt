package com.tokopedia.loginregister.external_register.ovo.view.dialog

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.external_register.base.listener.BaseDialogConnectAccListener

/**
 * Created by Yoris Prayogo on 23/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class OvoAccountDialog {

    companion object {
        fun showConnectDialogUnify(context: Context?, listener: BaseDialogConnectAccListener?){
            context?.run {
                DialogUnify(context, DialogUnify.VERTICAL_ACTION, DialogUnify.WITH_ILLUSTRATION).apply {
                    setTitle(getString(R.string.ovo_connect_account_title))
                    setDescription(getString(R.string.ovo_connect_account_description))
                    setImageDrawable(R.drawable.img_ovo_collaboration)
                    setPrimaryCTAText(getString(R.string.dialog_register_account_positive_btn_text))
                    setSecondaryCTAText(getString(R.string.dialog_register_account_negative_btn_text))
                    setPrimaryCTAClickListener {
                        listener?.onDialogPositiveBtnClicked()
                        dismiss()
                    }
                    setSecondaryCTAClickListener {
                        listener?.onDialogNegativeBtnClicked()
                        dismiss()
                    }
                }.show()
            }
        }

        fun showRegisterDialogUnify(context: Context?, phoneNo: String = "", listener: BaseDialogConnectAccListener?){
            context?.run {
                DialogUnify(context, DialogUnify.VERTICAL_ACTION, DialogUnify.WITH_ILLUSTRATION).apply {
                    setTitle(getString(R.string.ovo_register_account_title))
                    setDescription(getString(R.string.ovo_register_account_description, phoneNo))
                    setPrimaryCTAText(getString(R.string.dialog_register_account_positive_btn_text))
                    setSecondaryCTAText(getString(R.string.dialog_register_account_negative_btn_text))
                    setImageDrawable(R.drawable.img_ovo_collaboration)
                    setPrimaryCTAClickListener {
                        listener?.onDialogPositiveBtnClicked()
                        dismiss()
                    }
                    setSecondaryCTAClickListener {
                        listener?.onDialogNegativeBtnClicked()
                        dismiss()
                    }
                }.show()
            }
        }
    }
}