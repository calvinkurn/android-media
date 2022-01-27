package com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.databinding.ItemServerLoggerListBinding
import com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter.ServerChannelAdapter
import com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter.ServerLoggerListener
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ItemServerLoggerUiModel
import com.tokopedia.analyticsdebugger.serverlogger.utils.ServerLoggerItemDecoration
import com.tokopedia.kotlin.extensions.view.isZero

class ItemServerLoggerListViewHolder(
    view: View,
    private val serverLoggerListener: ServerLoggerListener
) : AbstractViewHolder<ItemServerLoggerUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_server_logger_list
    }

    private val binding = ItemServerLoggerListBinding.bind(itemView)

    private var serverChannelAdapter: ServerChannelAdapter? = null

    override fun bind(element: ItemServerLoggerUiModel?) {
        if (element == null) return

        with(binding) {
            tvTagSL.text = element.tag
            tvPrioritySL.text = element.priority
            tvMessageSL.text = element.previewMessage
            tvDateTimeSL.text = element.dateTime

            setupRecyclerView(element.serverChannel)

            root.setOnClickListener {
                serverLoggerListener.onItemClicked(element)
            }
        }
    }

    private fun ItemServerLoggerListBinding.setupRecyclerView(data: List<String>) {
        serverChannelAdapter = ServerChannelAdapter()
        rvSLChannel.run {
            if (itemDecorationCount.isZero()) {
                addItemDecoration(ServerLoggerItemDecoration())
            }
            layoutManager =
                LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = serverChannelAdapter
            setServerChannelList(data)
        }
    }

    private fun setServerChannelList(data: List<String>) {
        serverChannelAdapter?.setServerChannelList(data)
    }
}