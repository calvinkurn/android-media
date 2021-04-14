package com.tokopedia.sellerorder.common.presenter.bottomsheet

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.google.firebase.crashlytics.internal.common.CommonUtils.hideKeyboard
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.SomBottomSheet
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.bottomsheet_cancel_order.view.*

class SomOrderEditAwbBottomSheet(context: Context) : SomBottomSheet(context) {

    companion object {
        private const val PADDING_WHEN_KEYBOARD_OPEN = 325
        private const val PADDING_WHEN_KEYBOARD_CLOSED = 0
    }

    private var listener: SomOrderEditAwbBottomSheetListener? = null
    private var childView: View? = null

    init {
        childView = inflate(context, R.layout.bottomsheet_cancel_order, null).apply {
            tf_cancel_notes?.clearFocus()
            tf_cancel_notes?.setLabelStatic(true)
            tf_cancel_notes?.setMessage(context.getString(R.string.change_no_resi_notes))
            tf_cancel_notes?.textFieldInput?.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    val parentView = childView?.parent as? View ?: return@setOnFocusChangeListener
                    parentView.setPadding(parentView.paddingLeft, parentView.paddingTop, parentView.paddingRight, PADDING_WHEN_KEYBOARD_OPEN.toPx())
                } else {
                    val parentView = childView?.parent as? View ?: return@setOnFocusChangeListener
                    parentView.setPadding(parentView.paddingLeft, parentView.paddingTop, parentView.paddingRight, PADDING_WHEN_KEYBOARD_CLOSED)
                    hideKeyboard(context, tf_cancel_notes)
                }
            }
            tf_cancel_notes?.textFieldInput?.hint = context.getString(R.string.change_no_resi_hint)
            btn_cancel_order_canceled?.setOnClickListener { dismiss() }
            btn_cancel_order_confirmed?.text = context.getString(R.string.change_no_resi_btn_ubah)
            btn_cancel_order_confirmed?.setOnClickListener {
                dismiss()
                listener?.onEditAwbButtonClicked(tf_cancel_notes?.textFieldInput?.text.toString())
            }
        }
    }

    fun init(view: ViewGroup) {
        super.init(view, requireNotNull(childView), true)
    }

    fun setListener(listener: SomOrderEditAwbBottomSheetListener) {
        this.listener = listener
    }

    interface SomOrderEditAwbBottomSheetListener {
        fun onEditAwbButtonClicked(cancelNotes: String)
    }
}