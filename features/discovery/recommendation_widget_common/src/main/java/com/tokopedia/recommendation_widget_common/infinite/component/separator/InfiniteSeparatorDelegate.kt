package com.tokopedia.recommendation_widget_common.infinite.component.separator

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.ItemInfiniteSeparatorBinding
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationUiModel

class InfiniteSeparatorDelegate :
    TypedAdapterDelegate<InfiniteSeparatorUiModel, InfiniteRecommendationUiModel, InfiniteSeparatorViewHolder>(
        R.layout.item_infinite_separator
    ) {
    override fun onBindViewHolder(item: InfiniteSeparatorUiModel, holder: InfiniteSeparatorViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): InfiniteSeparatorViewHolder {
        val binding = ItemInfiniteSeparatorBinding.bind(basicView)
        return InfiniteSeparatorViewHolder(binding)
    }

}
