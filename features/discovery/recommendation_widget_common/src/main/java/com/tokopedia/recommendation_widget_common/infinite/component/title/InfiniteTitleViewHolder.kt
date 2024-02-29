package com.tokopedia.recommendation_widget_common.infinite.component.title

import com.tokopedia.home_component_header.view.HomeComponentHeaderListener
import com.tokopedia.recommendation_widget_common.databinding.ItemInfiniteTitleBinding
import com.tokopedia.recommendation_widget_common.extension.mapToChannelHeader
import com.tokopedia.recommendation_widget_common.infinite.main.InfiniteRecommendationCallback
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationViewHolder
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.home_component_header.R as home_component_headerR

class InfiniteTitleViewHolder(
    private val binding: ItemInfiniteTitleBinding,
    private val callback: InfiniteRecommendationCallback,
    private val headingType: Int = 0
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
        if(headingType != 0) {
            binding.recommendationHeaderView.findViewById<Typography?>(home_component_headerR.id.header_title)?.setType(headingType)
        }
    }
}
