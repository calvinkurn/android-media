package com.tokopedia.product.manage.item.video.view.adapter.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.video.view.model.TitleVideoRecommendationViewModel

class TitleVideoRecommendationViewHolder(itemView: View) : AbstractViewHolder<TitleVideoRecommendationViewModel>(itemView) {

    override fun bind(titleVideoRecommendationViewModel: TitleVideoRecommendationViewModel) {
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_add_video_recommendation_title
    }
}