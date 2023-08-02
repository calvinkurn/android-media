package com.tokopedia.inbox.universalinbox.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxMenuSeparatorViewHolder
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuSeparatorUiModel

class UniversalInboxMenuSeparatorDelegate:
    TypedAdapterDelegate<UniversalInboxMenuSeparatorUiModel, Any, UniversalInboxMenuSeparatorViewHolder>(
        UniversalInboxMenuSeparatorViewHolder.LAYOUT
    ) {
    override fun onBindViewHolder(
        item: UniversalInboxMenuSeparatorUiModel,
        holder: UniversalInboxMenuSeparatorViewHolder
    ) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = true
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): UniversalInboxMenuSeparatorViewHolder {
        return UniversalInboxMenuSeparatorViewHolder(basicView)
    }

}
