package com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.FeedRecommendationViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.kotlin.extensions.view.hide
import kotlinx.android.synthetic.main.item_feed_recommendation.view.*

/**
 * @author by milhamj on 20/12/18.
 */
class FeedRecommendationViewHolder(v: View,
                                   private val listener: RecommendationCardAdapter.RecommendationCardListener,
                                   private val cardTitleListener: CardTitleView.CardTitleListener)
    : AbstractViewHolder<FeedRecommendationViewModel>(v) {

    private var cardAdapter: RecommendationCardAdapter? = null

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_feed_recommendation
    }

    override fun bind(element: FeedRecommendationViewModel?) {
        if (element == null) {
            itemView.hide()
            return
        }

        cardAdapter = RecommendationCardAdapter(element.cards, adapterPosition, listener)
        itemView.recommendationRv.adapter = cardAdapter
        if (element.title.text.isNotEmpty()) {
            itemView.cardTitle.bind(element.title, element.template.cardrecom.title)
            itemView.cardTitle.listener = cardTitleListener
        } else{
            itemView.cardTitle.visibility = View.GONE
        }
    }

    override fun bind(element: FeedRecommendationViewModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty() || payloads[0] !is Int) {
            return
        }

        val position = payloads[0] as Int
        if (cardAdapter?.list?.size ?: 0 > position) {
            cardAdapter?.notifyItemChanged(position)
        }
    }

    override fun onViewRecycled() {
        cardAdapter?.list?.forEachIndexed { index, _ ->
            val holder = itemView.recommendationRv.findViewHolderForAdapterPosition(index)
            (holder as? RecommendationCardAdapter.RecommendationCardViewHolder)?.let {
                cardAdapter?.onViewRecycled(it)
            }
        }
    }
}