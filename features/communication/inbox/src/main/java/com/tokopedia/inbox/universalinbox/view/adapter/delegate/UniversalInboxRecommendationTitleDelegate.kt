package com.tokopedia.inbox.universalinbox.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxRecommendationTitleViewHolder
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationTitleUiModel

class UniversalInboxRecommendationTitleDelegate:
TypedAdapterDelegate<UniversalInboxRecommendationTitleUiModel, Any, UniversalInboxRecommendationTitleViewHolder>(
    UniversalInboxRecommendationTitleViewHolder.LAYOUT
) {
    override fun onBindViewHolder(
        item: UniversalInboxRecommendationTitleUiModel,
        holder: UniversalInboxRecommendationTitleViewHolder
    ) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = true
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): UniversalInboxRecommendationTitleViewHolder {
        return UniversalInboxRecommendationTitleViewHolder(basicView)
    }
}
