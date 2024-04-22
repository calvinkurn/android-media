package com.tokopedia.home_account.view.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.view.viewholder.TdnBannerViewHolder
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel

class HomeAccountTdnBannerDelegate :
    TypedAdapterDelegate<TopAdsImageUiModel, Any, TdnBannerViewHolder>(
        TdnBannerViewHolder.LAYOUT
    ) {

    override fun onBindViewHolder(item: TopAdsImageUiModel, holder: TdnBannerViewHolder) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = true
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): TdnBannerViewHolder {
        return TdnBannerViewHolder(basicView)
    }
}
