package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.EMPTY
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

    override fun bind(element: ItemPenaltyTickerUiModel) {
        binding?.tickerPenaltyPage?.run {
            tickerTitle = element.title
            setHtmlDescription(getDescriptionString(element))
            setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    listener?.onDescriptionClicked(linkUrl.toString())
                }

                override fun onDismiss() {
                    // No-op
                }
            })
        }
    }

    private fun getDescriptionString(element: ItemPenaltyTickerUiModel): String {
        return try {
            itemView.context?.getString(
                R.string.desc_ticker_penalty,
                element.tickerMessage,
                element.webUrl.orEmpty(),
                element.actionText.orEmpty()
            ).orEmpty()
        } catch (expected: Exception) {
            String.EMPTY
        }
    }

    companion object {
        val LAYOUT = R.layout.item_penalty_ticker
    }

}
