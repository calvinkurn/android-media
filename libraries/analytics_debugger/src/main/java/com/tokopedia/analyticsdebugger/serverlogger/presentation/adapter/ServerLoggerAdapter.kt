package com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter.diffutil.ServerLoggerDiffUtilCallback
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.BaseServerLoggerUiModel
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ServerLoggerPriorityUiModel

class ServerLoggerAdapter(serverLoggerAdapterTypeFactory: ServerLoggerAdapterTypeFactory) :
    BaseListAdapter<Visitable<*>, ServerLoggerAdapterTypeFactory>(serverLoggerAdapterTypeFactory) {

    fun setServerLoggerData(penaltyListUiModel: List<BaseServerLoggerUiModel>) {
        val diffCallback = ServerLoggerDiffUtilCallback(visitables, penaltyListUiModel)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(penaltyListUiModel)
        diffResult.dispatchUpdatesTo(this)
    }

    fun addServerLoggerListData(serverLoggerList: List<BaseServerLoggerUiModel>) {
        visitables.addAll(serverLoggerList)
        notifyItemRangeInserted(visitables.size, serverLoggerList.size)
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