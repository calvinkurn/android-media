package com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.FeedRecommendationViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.kotlin.extensions.view.gone
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
            itemView.gone()
            return
        }

        cardAdapter = RecommendationCardAdapter(element.cards, adapterPosition, listener)
        itemView.recommendationRv.adapter = cardAdapter

        itemView.cardTitle.bind(element.title, element.template.cardrecom.title)
        itemView.cardTitle.listener = cardTitleListener
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
}