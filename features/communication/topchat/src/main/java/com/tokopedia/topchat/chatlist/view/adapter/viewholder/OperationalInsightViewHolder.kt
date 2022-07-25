package com.tokopedia.topchat.chatlist.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.domain.pojo.operational_insight.ShopChatTicker
import com.tokopedia.topchat.chatlist.view.listener.ChatListItemListener
import com.tokopedia.topchat.databinding.ItemTickerChatPerformanceBinding
import com.tokopedia.utils.view.binding.viewBinding

class OperationalInsightViewHolder(
    itemView: View,
    var listener: ChatListItemListener
) : AbstractViewHolder<ShopChatTicker>(itemView) {

    private var binding: ItemTickerChatPerformanceBinding? by viewBinding()

    override fun bind(element: ShopChatTicker?) {
        element?.let {
            bindColor()
            bindListener(it)
        }
    }

    private fun bindColor() {

    }

    private fun bindListener(element: ShopChatTicker) {
        binding?.layoutTickerChatPerformance?.setOnClickListener {
            listener.onOperationalInsightTickerClicked(element)
        }

        binding?.iconCloseTickerChatPerformance?.setOnClickListener {
            listener.onOperationalInsightCloseButtonClicked(element)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_ticker_chat_performance
    }
}