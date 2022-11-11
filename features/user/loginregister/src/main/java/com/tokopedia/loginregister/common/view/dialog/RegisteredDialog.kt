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
                dialog.setDescription(
                    String.format(
                        resources.getString(R.string.reigster_page_phone_number_already_registered_info),
                        phone
                    )
                )
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
                    String.format(
                        resources.getString(
                            R.string.email_already_registered_info
                        ), email
                    )
                )
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
                    String.format(
                        resources.getString(
                            R.string.email_not_registered_info
                        ), email
                    )
                )
                dialog.setPrimaryCTAText(getString(R.string.not_registered_yes))
                dialog.setSecondaryCTAText(getString(R.string.already_registered_no))
                return dialog
            }
            return null
        }

        fun createRedefineRegisterEmailOfferLogin(context: Context, email: String): DialogUnify {
            val offerToLoginDialog =
                DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)

            offerToLoginDialog.apply {
                setTitle(context.getString(R.string.register_email_dialog_offer_login_title))
                setDescription(
                    context.getString(
                        R.string.register_email_dialog_offer_login_subtitle,
                        email
                    )
                )
                setPrimaryCTAText(context.getString(R.string.register_email_dialog_offer_login_primary_button))
                setSecondaryCTAText(context.getString(R.string.register_email_dialog_offer_login_secondary_button))
            }

            return offerToLoginDialog
        }

        fun createRedefineRegisterInputPhoneOfferLogin(
            context: Context,
            phone: String
        ): DialogUnify {
            val offerToLoginDialog =
                DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)

            offerToLoginDialog.apply {
                setTitle(context.getString(R.string.register_email_input_phone_dialog_offer_login_title))
                setDescription(
                    context.getString(
                        R.string.register_email_input_phone_dialog_offer_login_subtitle,
                        phone
                    )
                )
                setPrimaryCTAText(context.getString(R.string.register_email_dialog_offer_login_primary_button))
                setSecondaryCTAText(context.getString(R.string.register_email_dialog_offer_login_secondary_button))
            }

            return offerToLoginDialog
        }

        fun createRedefineRegisterInputPhoneFailed(context: Context): DialogUnify {
            val failedDialog =
                DialogUnify(context, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)

            failedDialog.apply {
                setTitle(context.getString(R.string.register_email_input_phone_dialog_failed_title))
                setDescription(context.getString(R.string.register_email_input_phone_dialog_failed_subtitle))
                setPrimaryCTAText(context.getString(R.string.register_email_input_phone_dialog_failed_primary_button))
                setSecondaryCTAText(context.getString(R.string.register_email_input_phone_dialog_failed_secondary_button))
            }

            return failedDialog
        }

        fun createRedefineRegisterInputPhoneOfferSuccess(
            context: Context,
            phone: String
        ): DialogUnify {
            val confirmDialog =
                DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)

            confirmDialog.apply {
                setTitle(phone)
                setDescription(context.getString(R.string.register_email_input_phone_dialog_success_subtitle))
                setPrimaryCTAText(context.getString(R.string.register_email_input_phone_dialog_success_primary_button))
                setSecondaryCTAText(context.getString(R.string.register_email_input_phone_dialog_success_secondary_button))
            }

            return confirmDialog
        }
    }
}
