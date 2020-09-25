package com.tokopedia.utils.permission

import android.app.Activity
import android.text.Spanned
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.tokopedia.utils.R

class PermissionDialog(val context: Activity) {

    var titleTextView: TextView? = null
        private set
    private var desc: TextView? = null
    var btnCancel: TextView? = null
        private set
    var btnOk: TextView? = null
        private set

    val alertDialog: AlertDialog


    private fun layoutResId(): Int {
        return R.layout.permission_dialog
    }

    private fun initView(dialogView: View) {
        titleTextView = dialogView.findViewById(R.id.tv_title_dialog)
        desc = dialogView.findViewById(R.id.tv_desc_dialog)
        btnOk = dialogView.findViewById(R.id.btn_ok_dialog)
        btnCancel = dialogView.findViewById(R.id.btn_cancel_dialog)
    }

    fun show() {
        alertDialog.show()
    }

    fun dismiss() {
        alertDialog.dismiss()
    }

    fun setOnOkClickListener(onOkClickListener: View.OnClickListener) {
        btnOk!!.setOnClickListener(onOkClickListener)
    }

    fun setOnCancelClickListener(onCancelClickListener: View.OnClickListener?) {
        btnCancel!!.setOnClickListener(onCancelClickListener)
    }

    fun setTitle(title: String?) {
        titleTextView!!.text = title
    }

    fun setDesc(desc: String?) {
        this.desc!!.text = desc
    }

    fun setDesc(desc: Spanned?) {
        this.desc!!.text = desc
    }

    fun setBtnOk(title: String?) {
        btnOk!!.text = title
    }

    fun setBtnCancel(title: String?) {
        btnCancel!!.text = title
    }

    init {
        val dialogView = context.layoutInflater.inflate(layoutResId(), null)
        initView(dialogView)
        alertDialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .create()
        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)
    }
}