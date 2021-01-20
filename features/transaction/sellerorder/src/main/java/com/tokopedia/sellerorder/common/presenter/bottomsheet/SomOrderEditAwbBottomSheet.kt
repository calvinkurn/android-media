package com.tokopedia.sellerorder.common.presenter.bottomsheet

import android.os.Bundle
import android.view.View
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_cancel_order.view.*

class SomOrderEditAwbBottomSheet: BottomSheetUnify() {

    companion object {
        const val TAG = "SomOrderEditAwbBottomSheet"
    }

    private var listener: SomOrderEditAwbBottomSheetListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(SomConsts.TITLE_UBAH_RESI)
        isFullpage = false
        setCloseClickListener { dismiss() }
        val viewBottomSheetUbahResi = View.inflate(context, R.layout.bottomsheet_cancel_order, null).apply {
            tf_cancel_notes?.setLabelStatic(true)
            tf_cancel_notes?.setMessage(getString(R.string.change_no_resi_notes))
            tf_cancel_notes?.textFieldInput?.hint = getString(R.string.change_no_resi_hint)
            btn_cancel_order_canceled?.setOnClickListener { dismiss() }
            btn_cancel_order_confirmed?.text = getString(R.string.change_no_resi_btn_ubah)
            btn_cancel_order_confirmed?.setOnClickListener {
                dismiss()
                listener?.onEditAwbButtonClicked(tf_cancel_notes?.textFieldInput?.text.toString())
            }
        }
        setChild(viewBottomSheetUbahResi)
    }

    fun setListener(listener: SomOrderEditAwbBottomSheetListener) {
        this.listener = listener
    }

    interface SomOrderEditAwbBottomSheetListener {
        fun onEditAwbButtonClicked(cancelNotes: String)
    }
}