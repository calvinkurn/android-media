package com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.MoreMenuUiModel

class InformationTickerViewHolder(itemView: View): AbstractViewHolder<MoreMenuUiModel.InformationTicker>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_mvc_info_ticker
    }

    override fun bind(element: MoreMenuUiModel.InformationTicker) {}
}