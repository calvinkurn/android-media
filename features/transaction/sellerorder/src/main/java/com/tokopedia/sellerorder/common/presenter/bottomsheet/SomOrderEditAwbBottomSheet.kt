package com.tokopedia.sellerorder.common.presenter.bottomsheet

import android.content.Context
import android.view.View
import com.google.firebase.crashlytics.internal.common.CommonUtils.hideKeyboard
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.SomBottomSheet
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.databinding.BottomsheetCancelOrderBinding

class SomOrderEditAwbBottomSheet(
        context: Context
) : SomBottomSheet<BottomsheetCancelOrderBinding>(LAYOUT, true, true, false, false, false, SomConsts.TITLE_UBAH_RESI, context, true) {

    companion object {
        private val LAYOUT = R.layout.bottomsheet_cancel_order
    }

    private var listener: SomOrderEditAwbBottomSheetListener? = null

    override fun bind(view: View): BottomsheetCancelOrderBinding {
        return BottomsheetCancelOrderBinding.bind(view)
    }

    override fun setupChildView() {
        binding?.run {
            tfCancelNotes.clearFocus()
            tfCancelNotes.setLabelStatic(true)
            tfCancelNotes.setMessage(context.getString(R.string.change_no_resi_notes))
            tfCancelNotes.textFieldInput.hint = context.getString(R.string.change_no_resi_hint)
            btnCancelOrderCanceled.setOnClickListener {
                hideKeyboard(context, tfCancelNotes.rootView)
                dismiss()
            }
            btnCancelOrderConfirmed.text = context.getString(R.string.change_no_resi_btn_ubah)
            btnCancelOrderConfirmed.setOnClickListener {
                hideKeyboard(context, tfCancelNotes.rootView)
                dismiss()
                listener?.onEditAwbButtonClicked(tfCancelNotes.textFieldInput.text.toString())
            }
            handleHideKeyboardWhenClickOnBottomSheet()
        }
    }

    private fun handleHideKeyboardWhenClickOnBottomSheet() {
        bottomSheetLayout?.setOnClickListener {
            binding?.tfCancelNotes?.clearFocus()
        }
    }

    fun setListener(listener: SomOrderEditAwbBottomSheetListener) {
        this.listener = listener
    }

    interface SomOrderEditAwbBottomSheetListener {
        fun onEditAwbButtonClicked(cancelNotes: String)
    }
}