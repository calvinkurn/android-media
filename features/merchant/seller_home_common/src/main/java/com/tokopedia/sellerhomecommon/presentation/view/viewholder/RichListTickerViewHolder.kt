package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcRichListTickerItemBinding
import com.tokopedia.sellerhomecommon.presentation.model.BaseRichListItemUiModel

/**
 * Created by @ilhamsuaib on 06/05/23.
 */

class RichListTickerViewHolder(
    itemView: View
) : AbstractViewHolder<BaseRichListItemUiModel.TickerItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_rich_list_ticker_item
    }

    private val binding by lazy {
        ShcRichListTickerItemBinding.bind(itemView)
    }

    override fun bind(element: BaseRichListItemUiModel.TickerItemUiModel) {
        with(binding.tickerShcRichList) {
            setHtmlDescription(element.tickerDescription)
        }
    }
}