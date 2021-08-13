package com.tokopedia.home_account.view.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.view.viewholder.TopAdsHeadlineViewHolder
import com.tokopedia.home_account.view.viewmodel.topads.TopadsHeadlineUiModel
import com.tokopedia.user.session.UserSessionInterface

class TopAdsHeadlineViewDelegate(private val userSessionInterface: UserSessionInterface) : TypedAdapterDelegate<TopadsHeadlineUiModel, Any, TopAdsHeadlineViewHolder>(
TopAdsHeadlineViewHolder.LAYOUT
) {

    override fun onBindViewHolder(item: TopadsHeadlineUiModel, holder: TopAdsHeadlineViewHolder) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = true
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): TopAdsHeadlineViewHolder {
        return TopAdsHeadlineViewHolder(basicView, userSessionInterface)
    }
}