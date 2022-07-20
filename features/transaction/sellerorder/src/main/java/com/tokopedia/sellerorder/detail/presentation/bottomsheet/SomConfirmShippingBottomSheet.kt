package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.content.Context
import android.view.View
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.SomBottomSheet
import com.tokopedia.sellerorder.databinding.PartialInfoLayoutBinding

class SomConfirmShippingBottomSheet(
        context: Context
) : SomBottomSheet<PartialInfoLayoutBinding>(LAYOUT, true, true, false, false, false, context.getString(R.string.automatic_shipping), context, true) {

    companion object {
        private val LAYOUT = R.layout.partial_info_layout
    }

    override fun bind(view: View): PartialInfoLayoutBinding {
        return PartialInfoLayoutBinding.bind(view)
    }

    override fun setupChildView() {
        binding?.buttonUnderstand?.setOnClickListener { dismiss() }
    }

    fun setInfoText(infoText: String) {
        binding?.tvConfirmInfo?.text = infoText
    }
}