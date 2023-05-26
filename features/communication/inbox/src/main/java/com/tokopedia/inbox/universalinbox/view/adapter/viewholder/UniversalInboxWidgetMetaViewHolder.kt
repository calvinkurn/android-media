package com.tokopedia.inbox.universalinbox.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.inbox.databinding.UniversalInboxWidgetMetaItemBinding
import com.tokopedia.inbox.R
import com.tokopedia.inbox.universalinbox.view.adapter.UniversalInboxWidgetAdapter
import com.tokopedia.inbox.universalinbox.view.customview.UniversalInboxWidgetLayoutManager
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxWidgetListener
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetMetaUiModel
import com.tokopedia.utils.view.binding.viewBinding

class UniversalInboxWidgetMetaViewHolder(
    itemView: View,
    private val listener: UniversalInboxWidgetListener
): BaseViewHolder(itemView) {

    private val context = itemView.context
    private val binding: UniversalInboxWidgetMetaItemBinding? by viewBinding()
    private var adapter: UniversalInboxWidgetAdapter? = null

    fun bind(uiModel: UniversalInboxWidgetMetaUiModel) {
        if (uiModel.widgetList.isNotEmpty()) {
            adapter = UniversalInboxWidgetAdapter(uiModel.widgetList, listener)
            binding?.inboxRvWidgetMeta?.layoutManager = UniversalInboxWidgetLayoutManager(
                context = context,
                orientation = LinearLayoutManager.HORIZONTAL,
                reverseLayout = false,
                uiModel = uiModel
            )
            binding?.inboxRvWidgetMeta?.adapter = adapter
            binding?.inboxRvWidgetMeta?.setHasFixedSize(true)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_inbox_widget_meta_item
    }
}
