package com.tokopedia.loginregister.common.view.dialog

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.loginregister.R

/**
 * Created by Yoris Prayogo on 15/12/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class ProceedWithPhoneDialog {

    companion object {
        fun createDialog(context: Context?, title: String): DialogUnify? {
            context?.run {
                val dialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
                dialog.setTitle(title)
                dialog.setDescription(getString(R.string.phone_number_not_registered_info))
                dialog.setPrimaryCTAText(getString(R.string.proceed_with_phone_number))
                dialog.setSecondaryCTAText(getString(R.string.already_registered_no))
                return dialog
            }
            return null
        }

    }
}