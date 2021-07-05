package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.content.Context
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.SomBottomSheet
import kotlinx.android.synthetic.main.partial_info_layout.view.*

class SomConfirmShippingBottomSheet(
        context: Context
) : SomBottomSheet(LAYOUT, true, true, false, context.getString(R.string.automatic_shipping), context, true) {

    companion object {
        private val LAYOUT = R.layout.partial_info_layout
    }
    private var onDismiss: () -> Unit = {}

    override fun setupChildView() {
        childViews?.run {
            button_understand?.setOnClickListener { dismiss() }
        }
    }

    override fun dismiss() {
        super.dismiss()
        onDismiss()
    }

    fun setInfoText(infoText: String) {
        childViews?.tv_confirm_info?.text = infoText
    }

    fun setOnDismiss(onDismiss: () -> Unit) {
        this.onDismiss = onDismiss
    }
}