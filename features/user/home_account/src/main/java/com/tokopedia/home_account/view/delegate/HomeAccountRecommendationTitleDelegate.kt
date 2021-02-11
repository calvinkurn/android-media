package com.tokopedia.home_account.view.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.data.model.RecommendationTitleView
import com.tokopedia.home_account.view.viewholder.ReommendationTitleViewHolder

class HomeAccountRecommendationTitleDelegate :
        TypedAdapterDelegate<RecommendationTitleView, Any, ReommendationTitleViewHolder>(
                ReommendationTitleViewHolder.LAYOUT
        ) {

    override fun onBindViewHolder(item: RecommendationTitleView, holder: ReommendationTitleViewHolder) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = true
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ReommendationTitleViewHolder {
        return ReommendationTitleViewHolder(basicView)
    }
}