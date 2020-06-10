package com.tokopedia.vouchercreation.create.view.viewholder.vouchertype.item

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.UnavailableTickerUiModel
import kotlinx.android.synthetic.main.item_mvc_unavailable_ticker.view.*

class UnavailableTickerViewHolder(itemView: View): AbstractViewHolder<UnavailableTickerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_mvc_unavailable_ticker
    }

    override fun bind(element: UnavailableTickerUiModel) {
        with(itemView) {
            unavailableTicker?.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    // No op
                }

                override fun onDismiss() {
                    element.onDismissClicked()
                }
            })
        }
    }
}