package com.tokopedia.recommendation_widget_common.infinite.component.product

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.ItemInfiniteProductBinding
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationUiModel

class InfiniteProductDelegate :
    TypedAdapterDelegate<InfiniteProductUiModel, InfiniteRecommendationUiModel, InfiniteProductViewHolder>(
        R.layout.item_infinite_product
    ) {
    override fun onBindViewHolder(item: InfiniteProductUiModel, holder: InfiniteProductViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): InfiniteProductViewHolder {
        val binding = ItemInfiniteProductBinding.bind(basicView)
        return InfiniteProductViewHolder(binding)
    }
}
