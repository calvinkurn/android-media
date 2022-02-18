package com.tokopedia.videoTabComponent.viewmodel

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.videoTabComponent.domain.delegate.PlaySlotTabViewAdapterDelegate
import com.tokopedia.videoTabComponent.domain.delegate.PlayWidgetViewAdapterDelegate
import com.tokopedia.videoTabComponent.callback.PlaySlotTabCallback
import com.tokopedia.videoTabComponent.domain.model.data.*
import com.tokopedia.videoTabComponent.view.coordinator.PlayWidgetCoordinatorVideoTab

/**
 * Created by meyta.taliti on 28/01/22.
 */
class VideoTabAdapter(
    coordinator: PlayWidgetCoordinatorVideoTab, listener: PlaySlotTabCallback, activity: Activity
) : BaseDiffUtilAdapter<PlayFeedUiModel>(isFlexibleType = true) {

    private var mCurrentHeader: Pair<Int, RecyclerView.ViewHolder>? = null
    var slotPosition: Int? = null

    init {
        delegatesManager
            .addDelegate(PlayWidgetViewAdapterDelegate.Jumbo(coordinator))
            .addDelegate(PlayWidgetViewAdapterDelegate.Large(coordinator))
            .addDelegate(PlayWidgetViewAdapterDelegate.Medium(coordinator))
            .addDelegate(PlaySlotTabViewAdapterDelegate.SlotTab(listener, activity))
    }

    override fun areItemsTheSame(oldItem: PlayFeedUiModel, newItem: PlayFeedUiModel): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: PlayFeedUiModel, newItem: PlayFeedUiModel): Boolean {
        return oldItem == newItem
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
    }

    fun setCurrentHeader(currentHeader: Pair<Int, RecyclerView.ViewHolder>?) {
        mCurrentHeader = currentHeader
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    fun getCurrentHeader() = mCurrentHeader

    fun updateList(mappedData: List<PlayFeedUiModel>) {
        val newList = mutableListOf<PlayFeedUiModel>()
        for (item in itemList) {
            newList.add(item)
            if (item is PlaySlotTabMenuUiModel) break
        }
        if (slotPosition == null) slotPosition = newList.size - 1
        newList.addAll(mappedData)
        setItems(newList)
        notifyDataSetChanged()

        /*itemList.forEachIndexed { index, playFeedUiModel ->
            if (getItem(index) is PlayWidgetLargeUiModel && getItem(index - 1) is PlaySlotTabMenuUiModel) {
                var item: PlayWidgetLargeUiModel ?= null
                mappedData.forEach { if (it is PlayWidgetLargeUiModel) item = it }

                item?.let {
                    (getItem(index) as PlayWidgetLargeUiModel).model = it.model
                }
                notifyItemChanged(index)
                return@forEachIndexed
            }
        }*/
    }

    fun isStickyHeaderView(it: Int): Boolean {
        return getItem(it) is PlaySlotTabMenuUiModel
    }

}