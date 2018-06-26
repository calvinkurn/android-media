package com.tokopedia.product.edit.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.viewmodel.VideoRecommendationViewModel

class VideoRecommendationViewHolder(itemView: View) : AbstractViewHolder<VideoRecommendationViewModel>(itemView) {

    init {
        findViews(itemView)
    }

    private fun findViews(view: View) {
    }

    override fun bind(videoRecommendationViewModel: VideoRecommendationViewModel) {
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_add_video_recommendation
    }
}