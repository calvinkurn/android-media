package com.tokopedia.design.dialog

import android.app.Activity
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


class AccessRequestFragmentDialog : DialogFragment() {

    private var layoutResId = R.layout.permission_fragment_dialog
    private var buttonAccept: String? = null
    private var buttonDeny: String? = null
    private var title: String? = null
    private var body: String? = null
    private var fromClickButtons: Boolean = false

    private var accessRequestListener: IAccessRequestListener? = null

    fun setLayoutResId(resId: Int) {
        if (resId != 0)
            layoutResId = resId
    }

    fun setBodyText(bodyText: String) {
        body = bodyText
    }

    fun setTitle(titleText: String) {
        title = titleText
    }

    fun setPositiveButton(positiveButton: String) {
        buttonAccept = positiveButton
    }

    fun setNegativeButton(negativeButton: String?) {
        buttonDeny = negativeButton
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(layoutResId)
        val buttonAccept = dialog.findViewById<TextView>(R.id.button_accept)
        val buttonDeny = dialog.findViewById<TextView>(R.id.button_deny)
        val clickListener = AccessRequestClickListener()
        buttonAccept.setOnClickListener(clickListener)
        buttonDeny.setOnClickListener(clickListener)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        setCustomText(dialog)
    }

    private fun setCustomText(dialog: Dialog) {
        val buttonAccept = dialog.findViewById<TextView>(R.id.button_accept)
        if (!TextUtils.isEmpty(this.buttonAccept))
            buttonAccept.text = this.buttonAccept
        val buttonDeny = dialog.findViewById<TextView>(R.id.button_deny)
        if (!TextUtils.isEmpty(this.buttonDeny))
            buttonDeny.text = this.buttonDeny
        else if (this.buttonDeny == null)
            buttonDeny.visibility = View.GONE
        val tvtitle = dialog.findViewById<TextView>(R.id.tv_title_access)
        if (!TextUtils.isEmpty(this.title))
            tvtitle.text = this.title
        val tvbody = dialog.findViewById<TextView>(R.id.tv_description_permission)
        if (!TextUtils.isEmpty(this.body))
            tvbody.text = this.body
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        accessRequestListener = activity as IAccessRequestListener?
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        if (!fromClickButtons) {
            if (activity != null)
                activity!!.finish()
        }
    }


    inner class AccessRequestClickListener : View.OnClickListener {

        override fun onClick(v: View) {
            if (v.id == R.id.button_accept) {
                accessRequestListener!!.clickAccept()
                fromClickButtons = true
                dismiss()
            } else {
                accessRequestListener!!.clickDeny()
                fromClickButtons = true
                dismiss()
            }
        }
    }

    companion object {
        @JvmStatic
        val TAG = "ACCESS REQUEST FRAGMENT"

        @JvmStatic
        fun newInstance(): AccessRequestFragmentDialog {
            return AccessRequestFragmentDialog()
        }
    }
}
