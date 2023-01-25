package com.tokopedia.product.detail.postatc.component.recommendation

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.product.detail.R

class RecommendationDelegate : TypedAdapterDelegate<RecommendationUiModel, Any, RecommendationViewHolder>(R.layout.item_recommendation) {
    override fun onBindViewHolder(item: RecommendationUiModel, holder: RecommendationViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): RecommendationViewHolder {
        return RecommendationViewHolder(basicView)
    }
}
