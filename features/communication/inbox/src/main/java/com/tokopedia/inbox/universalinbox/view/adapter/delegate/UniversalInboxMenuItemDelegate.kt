package com.tokopedia.inbox.universalinbox.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxMenuItemViewHolder
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxMenuListener
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuUiModel

class UniversalInboxMenuItemDelegate(
    private val listener: UniversalInboxMenuListener
):
    TypedAdapterDelegate<UniversalInboxMenuUiModel, Any, UniversalInboxMenuItemViewHolder>(
        UniversalInboxMenuItemViewHolder.LAYOUT
    ) {
    override fun onBindViewHolder(
        item: UniversalInboxMenuUiModel,
        holder: UniversalInboxMenuItemViewHolder
    ) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = true
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): UniversalInboxMenuItemViewHolder {
        return UniversalInboxMenuItemViewHolder(basicView, listener)
    }

}
