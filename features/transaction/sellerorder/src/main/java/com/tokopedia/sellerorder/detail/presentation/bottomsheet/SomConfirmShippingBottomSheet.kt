package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.SomBottomSheet
import kotlinx.android.synthetic.main.partial_info_layout.view.*

class SomConfirmShippingBottomSheet(
        context: Context
) : SomBottomSheet(context) {
    private var childView: View? = null
    private var onDismiss: () -> Unit = {}

    init {
        childView = inflate(context, R.layout.partial_info_layout, null).apply {
            button_understand?.setOnClickListener { dismiss() }
        }
    }

    override fun dismiss() {
        super.dismiss()
        onDismiss()
    }

    fun setInfoText(infoText: String) {
        childView?.tv_confirm_info?.text = infoText
    }

    fun setOnDismiss(onDismiss: () -> Unit) {
        this.onDismiss = onDismiss
    }

    fun init(view: ViewGroup) {
        super.init(view, requireNotNull(childView), true)
        setTitle(context.getString(R.string.automatic_shipping))
        showCloseButton()
        hideKnob()
    }
}