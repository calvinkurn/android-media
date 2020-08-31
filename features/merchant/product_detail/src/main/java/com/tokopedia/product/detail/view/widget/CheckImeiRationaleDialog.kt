package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.product.detail.R

/**
 * Created by Yoris Prayogo on 26/03/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class CheckImeiRationaleDialog {

    companion object {

        private fun openSettingPage(context: Context?) {
            context?.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            })
        }

        fun showRationaleDialog(context: Context?, onPrimaryButtonClick: () -> Unit, onSecondaryButtonClick: () -> Unit){
            context?.run {
                val dialog = DialogUnify(this, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
                val view = View.inflate(context, R.layout.dialog_permission_phone_state, null)
                dialog.apply {
                    setChild(view)
                    setPrimaryCTAText(context.getString(R.string.check_imei_btn_rationale) ?: "")
                    setPrimaryCTAClickListener {
                        onPrimaryButtonClick()
                        openSettingPage(context)
                        dialog.dismiss()
                    }
                    setSecondaryCTAText(context.getString(R.string.check_imei_btn_later) ?: "")
                    setSecondaryCTAClickListener {
                        onSecondaryButtonClick()
                        dialog.dismiss()
                    }
                }
                dialog.show()
            }

        }
    }
}