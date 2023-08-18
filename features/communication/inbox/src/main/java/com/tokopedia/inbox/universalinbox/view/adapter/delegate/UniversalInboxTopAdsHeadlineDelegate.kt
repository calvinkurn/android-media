package com.tokopedia.inbox.universalinbox.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxTopAdsHeadlineViewHolder
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopadsHeadlineUiModel
import com.tokopedia.user.session.UserSessionInterface

class UniversalInboxTopAdsHeadlineDelegate (
    private val userSession: UserSessionInterface
): TypedAdapterDelegate<UniversalInboxTopadsHeadlineUiModel, Any, UniversalInboxTopAdsHeadlineViewHolder>(
UniversalInboxTopAdsHeadlineViewHolder.LAYOUT
) {

    override fun onBindViewHolder(
        item: UniversalInboxTopadsHeadlineUiModel,
        holder: UniversalInboxTopAdsHeadlineViewHolder
    ) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = true
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): UniversalInboxTopAdsHeadlineViewHolder {
        return UniversalInboxTopAdsHeadlineViewHolder(basicView, userSession)
    }
}
