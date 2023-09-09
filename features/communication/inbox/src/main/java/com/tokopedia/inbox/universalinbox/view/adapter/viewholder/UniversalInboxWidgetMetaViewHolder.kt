package com.tokopedia.inbox.universalinbox.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.inbox.R
import com.tokopedia.inbox.databinding.UniversalInboxWidgetMetaItemBinding
import com.tokopedia.inbox.universalinbox.view.adapter.UniversalInboxWidgetAdapter
import com.tokopedia.inbox.universalinbox.view.customview.UniversalInboxWidgetLayoutManager
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxWidgetListener
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetMetaUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding

class UniversalInboxWidgetMetaViewHolder(
    itemView: View,
    private val listener: UniversalInboxWidgetListener
) : AbstractViewHolder<UniversalInboxWidgetMetaUiModel>(itemView) {

    private val context = itemView.context
    private val binding: UniversalInboxWidgetMetaItemBinding? by viewBinding()
    private var adapter: UniversalInboxWidgetAdapter? = null

    override fun bind(uiModel: UniversalInboxWidgetMetaUiModel) {
        if (uiModel.widgetList.isNotEmpty()) {
            bindWidgetList(uiModel)
        } else {
            if (uiModel.isError) {
                bindWidgetLocalLoad()
            } else {
                binding?.inboxLocalLoadWidgetMeta?.hide()
            }
            binding?.inboxRvWidgetMeta?.hide()
        }
    }

    private fun bindWidgetList(uiModel: UniversalInboxWidgetMetaUiModel) {
        adapter = UniversalInboxWidgetAdapter(uiModel.widgetList, listener)
        binding?.inboxRvWidgetMeta?.layoutManager = UniversalInboxWidgetLayoutManager(
            context = context,
            orientation = LinearLayoutManager.HORIZONTAL,
            reverseLayout = false
        )
        binding?.inboxRvWidgetMeta?.adapter = adapter
        binding?.inboxRvWidgetMeta?.setHasFixedSize(true)
        binding?.inboxRvWidgetMeta?.show()
        binding?.inboxLocalLoadWidgetMeta?.hide()
    }

    private fun bindWidgetLocalLoad() {
        binding?.inboxLocalLoadWidgetMeta?.apply {
            progressState = false
            refreshBtn?.setOnClickListener {
                progressState = true
                listener.onRefreshWidgetMeta()
            }
            show()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_inbox_widget_meta_item
    }
}
