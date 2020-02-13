package com.tokopedia.search.result.shop.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.result.shop.presentation.model.ShopRecommendationTitleViewModel

internal class ShopRecommendationTitleViewHolder(
        itemView: View
) : AbstractViewHolder<ShopRecommendationTitleViewModel>(itemView) {

    companion object {
        @JvmField
        val LAYOUT = R.layout.search_result_shop_empty_recommendation_title_layout
    }

    override fun bind(element: ShopRecommendationTitleViewModel?) { }
}