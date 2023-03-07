package com.tokopedia.topchat.chatlist.view.adapter.viewholder

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
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
            bindColor(element)
            bindListener(it)
        }
    }

    private fun bindColor(element: ShopChatTicker) {
        if (element.isMaintain == true) {
            binding?.layoutTickerChatPerformance?.setBackgroundColor(
                MethodChecker.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN0)
            )
            binding?.tvTickerChatPerformance?.text = getSpannableString(
                getString(R.string.chat_performance_ticker_maintain),
                getGoodText(),
                com.tokopedia.unifyprinciples.R.color.Unify_GN500
            )
        } else {
            binding?.layoutTickerChatPerformance?.setBackgroundColor(
                MethodChecker.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_Y100)
            )
            binding?.tvTickerChatPerformance?.text = getSpannableString(
                getString(R.string.chat_performance_ticker),
                getNeedImprovementText(),
                com.tokopedia.unifyprinciples.R.color.Unify_YN500
            )
        }
    }

    private fun getGoodText(): String {
        return getString(R.string.topchat_operational_insight_good)
    }

    private fun getNeedImprovementText(): String {
        return getString(R.string.topchat_operational_insight_need_improvement)
    }

    private fun getSpannableString(
        completeString: String,
        spanString: String,
        color: Int
    ): SpannableString {
        val spannableString = SpannableString(completeString)
        try {
            val startPosition = completeString.indexOf(spanString)
            val endPosition = completeString.lastIndexOf(spanString) + spanString.length
            spannableString.setSpan(
                ForegroundColorSpan(MethodChecker.getColor(itemView.context, color)),
                startPosition,
                endPosition,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
        } catch (ignored: Throwable) {}
        return spannableString
    }

    private fun bindListener(element: ShopChatTicker) {
        binding?.layoutTickerChatPerformance?.setOnClickListener {
            listener.onOperationalInsightTickerClicked(element)
        }
        binding?.iconCloseTickerChatPerformance?.setOnClickListener {
            listener.onOperationalInsightCloseButtonClicked(element)
        }
        listener.onOperationalInsightTickerShown(element)
    }

    companion object {
        val LAYOUT = R.layout.item_ticker_chat_performance
    }
}