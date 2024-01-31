package com.tokopedia.recommendation_widget_common.infinite.component.title

import com.tokopedia.recommendation_widget_common.databinding.ItemInfiniteTitleBinding
import com.tokopedia.recommendation_widget_common.infinite.main.InfiniteRecommendationCallback
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationViewHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.header.RecommendationHeaderListener

class InfiniteTitleViewHolder(
    private val binding: ItemInfiniteTitleBinding,
    private val callback: InfiniteRecommendationCallback,
) : InfiniteRecommendationViewHolder<InfiniteTitleUiModel>(binding.root) {
    override fun bind(item: InfiniteTitleUiModel) {
        binding.recommendationHeaderView.bindData(
            data = item.recommendationWidget,
            listener = object : RecommendationHeaderListener {
                override fun onSeeAllClick(link: String) {
                    callback.onClickViewAll(item.recommendationWidget)
                }

                override fun onChannelExpired(widget: RecommendationWidget) { }
            }
        )
    }
}
