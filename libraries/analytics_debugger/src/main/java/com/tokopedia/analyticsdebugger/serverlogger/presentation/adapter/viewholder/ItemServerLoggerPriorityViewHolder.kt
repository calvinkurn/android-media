package com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.databinding.ItemServerLoggerPriorityListBinding
import com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter.PriorityServerLoggerAdapter
import com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter.ServerLoggerAdapter
import com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter.ServerLoggerListener
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ItemPriorityUiModel
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ServerLoggerPriorityUiModel
import com.tokopedia.analyticsdebugger.serverlogger.utils.ServerLoggerItemDecoration
import com.tokopedia.kotlin.extensions.view.isZero

class ItemServerLoggerPriorityViewHolder(
    view: View,
    private val serverLoggerListener: ServerLoggerListener
) : AbstractViewHolder<ServerLoggerPriorityUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_server_logger_priority_list
    }

    private val binding = ItemServerLoggerPriorityListBinding.bind(itemView)

    private var priorityServerLogger: PriorityServerLoggerAdapter? = null

    override fun bind(element: ServerLoggerPriorityUiModel?) {
        if (element == null) return
        with(binding) {
            setRecyclerViewPriority(element.priority)
        }
    }

    override fun bind(element: ServerLoggerPriorityUiModel?, payloads: MutableList<Any>) {
        if (element == null || payloads.isNullOrEmpty()) return

        when (payloads.getOrNull(0) as? Int) {
            ServerLoggerAdapter.PAYLOAD_SELECTED_CHIPS -> {
                setPriorityList(element.priority)
            }
        }
    }

    private fun ItemServerLoggerPriorityListBinding.setRecyclerViewPriority(data: List<ItemPriorityUiModel>) {
        priorityServerLogger = PriorityServerLoggerAdapter(serverLoggerListener)
        rvSLPriority.run {
            if (itemDecorationCount.isZero()) {
                addItemDecoration(ServerLoggerItemDecoration())
            }
            layoutManager =
                LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = priorityServerLogger
            setPriorityList(data)
        }
    }

    private fun setPriorityList(data: List<ItemPriorityUiModel>) {
        priorityServerLogger?.setPriorityList(data)
    }
}