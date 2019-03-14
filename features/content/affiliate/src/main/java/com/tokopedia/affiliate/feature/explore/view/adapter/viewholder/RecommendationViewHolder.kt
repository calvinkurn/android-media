package com.tokopedia.affiliate.feature.explore.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.explore.view.adapter.RecommendationAdapter
import com.tokopedia.affiliate.feature.explore.view.viewmodel.RecommendationViewModel
import kotlinx.android.synthetic.main.item_af_recommendation.view.*

/**
 * @author by milhamj on 14/03/19.
 */
class RecommendationViewHolder(v: View) : AbstractViewHolder<RecommendationViewModel>(v) {

    val adapter: RecommendationAdapter by lazy {
        RecommendationAdapter()
    }

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_af_recommendation
    }

    override fun bind(element: RecommendationViewModel?) {
        if (element == null) {
            return
        }

        adapter.list.clear()
        adapter.list.addAll(element.cards)
        adapter.notifyDataSetChanged()
        itemView.cardsRv.adapter = adapter

        itemView.titleView.bind(element.titleViewModel)
    }
}