package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeLoadingUiModel

class TransparencyFeeLoadingViewHolder(view: View?):
    AbstractViewHolder<TransparencyFeeLoadingUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_transparency_fee_loading
    }
    override fun bind(element: TransparencyFeeLoadingUiModel?) {
        //no op
    }
}
