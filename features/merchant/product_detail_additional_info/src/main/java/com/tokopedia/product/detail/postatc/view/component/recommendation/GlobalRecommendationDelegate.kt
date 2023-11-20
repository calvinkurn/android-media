package com.tokopedia.product.detail.postatc.view.component.recommendation

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ItemGlobalRecommendationBinding
import com.tokopedia.product.detail.postatc.base.PostAtcCallback
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel

class GlobalRecommendationDelegate(
    private val callback: PostAtcCallback,
) : TypedAdapterDelegate<RecommendationUiModel, PostAtcUiModel, GlobalRecommendationViewHolder>(R.layout.item_global_recommendation) {
    override fun onBindViewHolder(item: RecommendationUiModel, holder: GlobalRecommendationViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): GlobalRecommendationViewHolder {
        val binding = ItemGlobalRecommendationBinding.bind(basicView)
        return GlobalRecommendationViewHolder(binding, callback)
    }
}
