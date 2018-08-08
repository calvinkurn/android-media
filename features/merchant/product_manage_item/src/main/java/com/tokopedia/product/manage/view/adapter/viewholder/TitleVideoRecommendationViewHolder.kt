package com.tokopedia.product.edit.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.view.viewmodel.TitleVideoRecommendationViewModel

class TitleVideoRecommendationViewHolder(itemView: View) : AbstractViewHolder<TitleVideoRecommendationViewModel>(itemView) {

    override fun bind(titleVideoRecommendationViewModel: TitleVideoRecommendationViewModel) {
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_add_video_recommendation_title
    }
}