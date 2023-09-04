package com.tokopedia.inbox.universalinbox.view.adapter.delegate

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxRecommendationProductViewHolder
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

class UniversalInboxRecommendationDelegate(
    private val recommendationListener: RecommendationListener
) :
    TypedAdapterDelegate<RecommendationItem, Any, UniversalInboxRecommendationProductViewHolder>(
        UniversalInboxRecommendationProductViewHolder.LAYOUT
    ) {
    override fun onBindViewHolder(
        item: RecommendationItem,
        holder: UniversalInboxRecommendationProductViewHolder
    ) {
        holder.bind(item)
    }

    override fun onBindViewHolderWithPayloads(
        item: RecommendationItem,
        holder: UniversalInboxRecommendationProductViewHolder,
        payloads: Bundle
    ) {
        holder.bind(item, payloads)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): UniversalInboxRecommendationProductViewHolder {
        return UniversalInboxRecommendationProductViewHolder(basicView, recommendationListener)
    }
}
