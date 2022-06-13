package com.tokopedia.vouchercreation.shop.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.ItemMvcWarningTickerBinding
import com.tokopedia.vouchercreation.shop.detail.model.WarningTickerUiModel

class WarningTickerViewHolder(
        itemView: View?,
        private val onSelectAction: (dataKey: String) -> Unit
) : AbstractViewHolder<WarningTickerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_warning_ticker
    }

    private var binding: ItemMvcWarningTickerBinding? by viewBinding()

    override fun bind(element: WarningTickerUiModel) {
        binding?.apply {
            tickerWarning.setHtmlDescription(tickerWarning.context.getString(R.string.mvc_promo_code_warning))
            tickerWarning.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    onSelectAction(element.dataKey)
                }

                override fun onDismiss() {
                }
            })
        }
    }
}