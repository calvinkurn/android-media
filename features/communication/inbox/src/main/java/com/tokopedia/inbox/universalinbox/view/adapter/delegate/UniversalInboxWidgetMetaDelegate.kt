package com.tokopedia.inbox.universalinbox.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxWidgetMetaViewHolder
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxWidgetListener
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetMetaUiModel

class UniversalInboxWidgetMetaDelegate(
    private val listener: UniversalInboxWidgetListener
):
    TypedAdapterDelegate<UniversalInboxWidgetMetaUiModel, Any, UniversalInboxWidgetMetaViewHolder>(
        UniversalInboxWidgetMetaViewHolder.LAYOUT
) {
    override fun onBindViewHolder(
        item: UniversalInboxWidgetMetaUiModel,
        holder: UniversalInboxWidgetMetaViewHolder
    ) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = true
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): UniversalInboxWidgetMetaViewHolder {
        return UniversalInboxWidgetMetaViewHolder(basicView, listener)
    }

}
