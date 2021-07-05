package com.tokopedia.home_account.view.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.home_account.view.viewholder.ProductItemViewHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

class HomeAccountRecommendationItemDelegate (val listener: HomeAccountUserListener):
        TypedAdapterDelegate<RecommendationItem, Any, ProductItemViewHolder>(
                ProductItemViewHolder.LAYOUT
        ) {
    override fun onBindViewHolder(item: RecommendationItem, holder: ProductItemViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProductItemViewHolder {
        return ProductItemViewHolder(basicView, listener)
    }
}