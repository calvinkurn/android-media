package com.tokopedia.loginregister.external_register.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.external_register.base.listener.BaseDialogConnectAccListener
import kotlinx.android.synthetic.main.dialog_base_connect_account.view.*

/**
 * Created by Yoris Prayogo on 17/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class BaseDialogConnectAccount(val listener: BaseDialogConnectAccListener?,
                                    val title: String, val description: String, val imgDrawable: Int = 0): DialogFragment() {

    companion object {
        fun showDialog(title: String, description: String, imgDrawable: Int = 0, listener: BaseDialogConnectAccListener): BaseDialogConnectAccount{
            return BaseDialogConnectAccount(listener, title, description, imgDrawable)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val view = inflater.inflate(R.layout.dialog_base_connect_account, container, false)
        if(imgDrawable != 0) {
            view?.dialog_external_register_img?.setImageResource(imgDrawable)
        }else {
            view?.dialog_external_register_img?.hide()
        }
        view?.dialog_external_register_title?.text = title
        view?.dialog_external_register_description?.text = description

        view?.dialog_external_register_btn_positive?.setOnClickListener {
            listener?.onDialogPositiveBtnClicked()
            dismiss()
        }

        view?.dialog_external_register_btn_negative?.setOnClickListener {
            listener?.onDialogNegativeBtnClicked()
            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}