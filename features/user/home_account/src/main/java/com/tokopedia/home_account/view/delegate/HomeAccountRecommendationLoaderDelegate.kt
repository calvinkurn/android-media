package com.tokopedia.home_account.view.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.data.model.LoadMoreRecommendation
import com.tokopedia.home_account.view.viewholder.LoadMoreRecommendationViewHolder

class HomeAccountRecommendationLoaderDelegate :
        TypedAdapterDelegate<LoadMoreRecommendation, Any, LoadMoreRecommendationViewHolder>(
                LoadMoreRecommendationViewHolder.LAYOUT
        ) {

    override fun onBindViewHolder(item: LoadMoreRecommendation, holder: LoadMoreRecommendationViewHolder) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): LoadMoreRecommendationViewHolder {
        return LoadMoreRecommendationViewHolder(basicView)
    }
}