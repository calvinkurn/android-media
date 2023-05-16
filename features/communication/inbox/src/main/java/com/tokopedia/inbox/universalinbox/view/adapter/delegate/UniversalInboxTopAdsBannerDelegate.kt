package com.tokopedia.inbox.universalinbox.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxTopAdsBannerViewHolder
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopAdsBannerUiModel
import com.tokopedia.topads.sdk.listener.TdnBannerResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener

class UniversalInboxTopAdsBannerDelegate(
    private val tdnBannerResponseListener: TdnBannerResponseListener,
    private val topAdsClickListener: TopAdsImageViewClickListener,
): TypedAdapterDelegate<UniversalInboxTopAdsBannerUiModel, Any, UniversalInboxTopAdsBannerViewHolder>(
    UniversalInboxTopAdsBannerViewHolder.LAYOUT
) {
    override fun onBindViewHolder(
        item: UniversalInboxTopAdsBannerUiModel,
        holder: UniversalInboxTopAdsBannerViewHolder
    ) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = true
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): UniversalInboxTopAdsBannerViewHolder {
        return UniversalInboxTopAdsBannerViewHolder(
            basicView, tdnBannerResponseListener, topAdsClickListener)
    }
}
