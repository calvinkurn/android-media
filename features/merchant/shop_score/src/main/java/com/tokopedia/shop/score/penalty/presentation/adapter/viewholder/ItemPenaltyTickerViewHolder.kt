package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemPenaltyTickerBinding
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemPenaltyTickerListener
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyTickerUiModel
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.utils.view.binding.viewBinding

class ItemPenaltyTickerViewHolder(
    view: View,
    private val listener: ItemPenaltyTickerListener?
) : AbstractViewHolder<ItemPenaltyTickerUiModel>(view) {

    private var binding: ItemPenaltyTickerBinding? by viewBinding()

    override fun bind(element: ItemPenaltyTickerUiModel?) {
        binding?.tickerPenaltyPage?.run {
            setHtmlDescription(getString(R.string.desc_ticker_penalty))
            setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    listener?.onDescriptionClicked()
                }

                override fun onDismiss() {
                    // No-op
                }
            })
        }
    }

    companion object {
        val LAYOUT = R.layout.item_penalty_ticker
    }

}
