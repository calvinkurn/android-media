package com.tokopedia.inbox.universalinbox.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxRecommendationLoaderViewHolder
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationLoaderUiModel

class UniversalInboxRecommendationLoaderDelegate:
    TypedAdapterDelegate<UniversalInboxRecommendationLoaderUiModel, Any, UniversalInboxRecommendationLoaderViewHolder>(
        UniversalInboxRecommendationLoaderViewHolder.LAYOUT
    ) {
    override fun onBindViewHolder(
        item: UniversalInboxRecommendationLoaderUiModel,
        holder: UniversalInboxRecommendationLoaderViewHolder
    ) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = true
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): UniversalInboxRecommendationLoaderViewHolder {
        return UniversalInboxRecommendationLoaderViewHolder(basicView)
    }
}
