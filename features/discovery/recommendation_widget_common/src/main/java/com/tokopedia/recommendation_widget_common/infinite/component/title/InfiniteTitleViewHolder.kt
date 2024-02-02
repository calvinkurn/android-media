package com.tokopedia.recommendation_widget_common.infinite.component.title

import com.tokopedia.home_component_header.view.HomeComponentHeaderListener
import com.tokopedia.recommendation_widget_common.databinding.ItemInfiniteTitleBinding
import com.tokopedia.recommendation_widget_common.extension.mapToChannelHeader
import com.tokopedia.recommendation_widget_common.infinite.main.InfiniteRecommendationCallback
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationViewHolder

class InfiniteTitleViewHolder(
    private val binding: ItemInfiniteTitleBinding,
    private val callback: InfiniteRecommendationCallback,
) : InfiniteRecommendationViewHolder<InfiniteTitleUiModel>(binding.root) {
    override fun bind(item: InfiniteTitleUiModel) {
        binding.recommendationHeaderView.bind(
            channelHeader = item.recommendationWidget.mapToChannelHeader(),
            listener = object: HomeComponentHeaderListener {
                override fun onSeeAllClick(link: String) {
                    callback.onClickViewAll(item.recommendationWidget)
                }
            }
        )
    }
}
