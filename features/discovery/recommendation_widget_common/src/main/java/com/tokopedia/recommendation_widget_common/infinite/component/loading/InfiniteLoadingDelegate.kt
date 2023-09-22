package com.tokopedia.recommendation_widget_common.infinite.component.loading

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.ItemInfiniteLoadingBinding
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationUiModel

class InfiniteLoadingDelegate :
    TypedAdapterDelegate<InfiniteLoadingUiModel, InfiniteRecommendationUiModel, InfiniteLoadingViewHolder>(
        R.layout.item_infinite_loading
    ) {
    override fun onBindViewHolder(item: InfiniteLoadingUiModel, holder: InfiniteLoadingViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): InfiniteLoadingViewHolder {
        val binding = ItemInfiniteLoadingBinding.bind(basicView)
        return InfiniteLoadingViewHolder(binding)
    }

}
