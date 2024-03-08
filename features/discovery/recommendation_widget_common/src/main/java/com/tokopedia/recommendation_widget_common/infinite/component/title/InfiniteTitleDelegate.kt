package com.tokopedia.recommendation_widget_common.infinite.component.title

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.ItemInfiniteTitleBinding
import com.tokopedia.recommendation_widget_common.infinite.main.InfiniteRecommendationCallback
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationUiModel

class InfiniteTitleDelegate(
    private val callback: InfiniteRecommendationCallback,
    private val headingType: Int = 0
) :
    TypedAdapterDelegate<InfiniteTitleUiModel, InfiniteRecommendationUiModel, InfiniteTitleViewHolder>(
        R.layout.item_infinite_title
    ) {
    override fun onBindViewHolder(item: InfiniteTitleUiModel, holder: InfiniteTitleViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): InfiniteTitleViewHolder {
        val binding = ItemInfiniteTitleBinding.bind(basicView)
        return InfiniteTitleViewHolder(binding, callback, headingType)
    }
}
