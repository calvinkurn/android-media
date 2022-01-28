package com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter.diffutil.ServerLoggerDiffUtilCallback
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.BaseServerLoggerUiModel
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ServerLoggerPriorityUiModel
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ItemServerLoggerUiModel

class ServerLoggerAdapter(serverLoggerAdapterTypeFactory: ServerLoggerAdapterTypeFactory) :
    BaseListAdapter<Visitable<*>, ServerLoggerAdapterTypeFactory>(serverLoggerAdapterTypeFactory) {

    fun setServerLoggerData(serverLoggerUiModel: List<BaseServerLoggerUiModel>) {
        val diffCallback = ServerLoggerDiffUtilCallback(visitables, serverLoggerUiModel)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(serverLoggerUiModel)
        diffResult.dispatchUpdatesTo(this)
    }

    fun addServerLoggerListData(serverLoggerList: List<BaseServerLoggerUiModel>) {
        val lastIndex = visitables.size
        visitables.addAll(serverLoggerList)
        notifyItemRangeInserted(lastIndex, serverLoggerList.size)
    }

    fun removeServerLoggerList() {
        val serverLoggerListCount = visitables.count { it is ItemServerLoggerUiModel }
        visitables.removeAll { it is ItemServerLoggerUiModel }
        notifyItemRangeRemoved(visitables.size, serverLoggerListCount)
    }

    fun updateChipsSelected(position: Int) {
        val updateIndex = visitables.indexOfFirst { it is ServerLoggerPriorityUiModel }
        visitables.filterIsInstance<ServerLoggerPriorityUiModel>()
            .firstOrNull()?.priority?.mapIndexed { index, item ->
                if (index == position) {
                    item.isSelected = !item.isSelected
                } else {
                    item.isSelected = false
                }
            }
        if (updateIndex != RecyclerView.NO_POSITION) {
            notifyItemChanged(updateIndex, PAYLOAD_SELECTED_CHIPS)
        }
    }

    companion object {
        const val PAYLOAD_SELECTED_CHIPS = 608
    }
}