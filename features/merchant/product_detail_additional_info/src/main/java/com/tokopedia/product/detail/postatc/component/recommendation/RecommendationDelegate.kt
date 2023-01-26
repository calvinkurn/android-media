package com.tokopedia.product.detail.postatc.component.recommendation

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ItemRecommendationBinding
import com.tokopedia.product.detail.postatc.base.PostAtcListener
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel

class RecommendationDelegate(
    private val listener: PostAtcListener
) : TypedAdapterDelegate<RecommendationUiModel, PostAtcUiModel, RecommendationViewHolder>(R.layout.item_recommendation) {
    override fun onBindViewHolder(item: RecommendationUiModel, holder: RecommendationViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): RecommendationViewHolder {
        val binding = ItemRecommendationBinding.bind(basicView)
        return RecommendationViewHolder(binding, listener)
    }
}
