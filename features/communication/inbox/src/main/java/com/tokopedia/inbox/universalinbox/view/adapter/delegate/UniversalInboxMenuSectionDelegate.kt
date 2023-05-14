package com.tokopedia.inbox.universalinbox.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxMenuSectionViewHolder
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuSectionUiModel

class UniversalInboxMenuSectionDelegate:
    TypedAdapterDelegate<UniversalInboxMenuSectionUiModel, Any, UniversalInboxMenuSectionViewHolder>(
        UniversalInboxMenuSectionViewHolder.LAYOUT
    ) {

    override fun onBindViewHolder(
        item: UniversalInboxMenuSectionUiModel,
        holder: UniversalInboxMenuSectionViewHolder
    ) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = true
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): UniversalInboxMenuSectionViewHolder {
        return UniversalInboxMenuSectionViewHolder(basicView)
    }
}
