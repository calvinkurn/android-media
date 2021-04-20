package com.tokopedia.home_account.view.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.data.model.RecommendationTitleView
import com.tokopedia.home_account.view.viewholder.RecommendationTitleViewHolder

class HomeAccountRecommendationTitleDelegate :
        TypedAdapterDelegate<RecommendationTitleView, Any, RecommendationTitleViewHolder>(
                RecommendationTitleViewHolder.LAYOUT
        ) {

    override fun onBindViewHolder(item: RecommendationTitleView, holder: RecommendationTitleViewHolder) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = true
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): RecommendationTitleViewHolder {
        return RecommendationTitleViewHolder(basicView)
    }
}