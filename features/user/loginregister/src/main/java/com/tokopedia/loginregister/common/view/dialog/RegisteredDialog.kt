package com.tokopedia.loginregister.common.view.dialog

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.loginregister.R

/**
 * Created by Yoris Prayogo on 15/12/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class RegisteredDialog {

    companion object {
        fun createRegisteredPhoneDialog(context: Context?, phone: String): DialogUnify? {
            context?.run {
                val dialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
                dialog.setTitle(getString(R.string.phone_number_already_registered))
                dialog.setDescription(String.format(resources.getString(R.string.reigster_page_phone_number_already_registered_info), phone))
                dialog.setPrimaryCTAText(getString(R.string.already_registered_yes))
                dialog.setSecondaryCTAText(getString(R.string.already_registered_no))
                return dialog
            }
            return null
        }

        fun createRegisteredEmailDialog(context: Context?, email: String): DialogUnify? {
            context?.run {
                val dialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
                dialog.setTitle(getString(R.string.email_already_registered))
                dialog.setDescription(
                        String.format(resources.getString(
                                R.string.email_already_registered_info), email))
                dialog.setPrimaryCTAText(getString(R.string.already_registered_yes))
                dialog.setSecondaryCTAText(getString(R.string.already_registered_no))
                return dialog
            }
            return null
        }


        fun createNotRegisteredEmailDialog(context: Context?, email: String): DialogUnify? {
            context?.run {
                val dialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
                dialog.setTitle(getString(R.string.email_not_registered))
                dialog.setDescription(
                        String.format(resources.getString(
                                R.string.email_not_registered_info), email))
                dialog.setPrimaryCTAText(getString(R.string.not_registered_yes))
                dialog.setSecondaryCTAText(getString(R.string.already_registered_no))
                return dialog
            }
            return null
        }
    }
}