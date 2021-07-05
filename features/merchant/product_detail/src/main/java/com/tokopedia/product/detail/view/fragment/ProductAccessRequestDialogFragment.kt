package com.tokopedia.product.detail.view.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.tokopedia.design.R

/**
 * Created by Yehezkiel on 11/03/20
 */
class ProductAccessRequestDialogFragment : DialogFragment() {

    private var layoutResId = com.tokopedia.design.R.layout.permission_fragment
    private var buttonAccept: String? = null
    private var buttonDeny: String? = null
    private var title: String? = null
    private var body: String? = null
    private var fromClickButtons = false
    private var accessListener: Listener? = null

    fun setBodyText(bodyText: String?) {
        body = bodyText
    }

    fun setTitle(titleText: String?) {
        title = titleText
    }

    fun setNegativeButton(negativeButton: String?) {
        buttonDeny = negativeButton
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(layoutResId)
        val buttonAccept = dialog.findViewById<TextView>(com.tokopedia.design.R.id.button_accept)
        val buttonDeny = dialog.findViewById<TextView>(com.tokopedia.design.R.id.button_deny)
        buttonAccept.setOnClickListener {
            accessListener?.onAccept()
            fromClickButtons = true
            dismiss()
        }
        buttonDeny.setOnClickListener {
            accessListener?.onDecline()
            fromClickButtons = true
            dismiss()
        }
        return dialog
    }

    override fun onStart() {
        super.onStart()
        setCustomText(dialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (!fromClickButtons) {
            if (activity != null) activity!!.finish()
        }
    }

    fun setListener(listener: Listener) {
        accessListener = listener
    }

    private fun setCustomText(dialog: Dialog?) {
        val buttonAccept = dialog!!.findViewById<TextView>(com.tokopedia.design.R.id.button_accept)
        if (!TextUtils.isEmpty(this.buttonAccept)) buttonAccept.text = this.buttonAccept
        val buttonDeny = dialog.findViewById<TextView>(com.tokopedia.design.R.id.button_deny)
        if (!TextUtils.isEmpty(this.buttonDeny)) {
            buttonDeny.text = this.buttonDeny
        } else if (this.buttonDeny == null) {
            buttonDeny.visibility = View.GONE
        }
        val tvTitle = dialog.findViewById<TextView>(com.tokopedia.design.R.id.tv_title_access)
        if (!TextUtils.isEmpty(title)) tvTitle.text = title
        val tvBody = dialog.findViewById<TextView>(com.tokopedia.design.R.id.tv_description_permission)
        if (!TextUtils.isEmpty(body)) tvBody.text = body
    }

    interface Listener {
        fun onAccept()
        fun onDecline()
    }

}