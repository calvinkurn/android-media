package com.tokopedia.vouchercreation.shop.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.detail.model.WarningTickerUiModel
import kotlinx.android.synthetic.main.item_mvc_warning_ticker.view.*

class WarningTickerViewHolder(
        itemView: View?,
        private val onSelectAction: (dataKey: String) -> Unit
) : AbstractViewHolder<WarningTickerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_warning_ticker
    }

    override fun bind(element: WarningTickerUiModel) {
        with(itemView) {
            tickerWarning?.setHtmlDescription(context.getString(R.string.mvc_promo_code_warning))
            tickerWarning?.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    onSelectAction(element.dataKey)
                }

                override fun onDismiss() {
                }
            })
        }
    }
}