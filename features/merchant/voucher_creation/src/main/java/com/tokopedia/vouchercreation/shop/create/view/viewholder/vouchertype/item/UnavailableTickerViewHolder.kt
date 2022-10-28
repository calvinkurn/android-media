package com.tokopedia.vouchercreation.shop.create.view.viewholder.vouchertype.item

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.ItemMvcUnavailableTickerBinding
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertype.item.UnavailableTickerUiModel

class UnavailableTickerViewHolder(itemView: View): AbstractViewHolder<UnavailableTickerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_mvc_unavailable_ticker
    }

    private var binding: ItemMvcUnavailableTickerBinding? by viewBinding()

    override fun bind(element: UnavailableTickerUiModel) {
        binding?.unavailableTicker?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                //no op
            }

            override fun onDismiss() {
                element.onCloseTicker()
            }
        })
    }
}