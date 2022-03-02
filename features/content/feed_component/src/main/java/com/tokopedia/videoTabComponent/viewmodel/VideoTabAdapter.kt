package com.tokopedia.videoTabComponent.viewmodel

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalFeed
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.videoTabComponent.callback.PlaySlotTabCallback
import com.tokopedia.videoTabComponent.domain.delegate.PlaySlotTabViewAdapterDelegate
import com.tokopedia.videoTabComponent.domain.delegate.PlayWidgetViewAdapterDelegate
import com.tokopedia.videoTabComponent.domain.mapper.WIDGET_UPCOMING
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

    fun updateList(mappedData: List<PlayFeedUiModel>, sourceId: String, sourceType: String, filterCategory: String) {
        val feedPlayLehatSemuaApplink = "${ApplinkConst.FEED_PlAY_LIVE_DETAIL}?${ApplinkConstInternalFeed.PLAY_LIVE_PARAM_WIDGET_TYPE}=$WIDGET_UPCOMING&${ApplinkConstInternalFeed.PLAY_UPCOMING_SOURCE_ID}=$sourceId&${ApplinkConstInternalFeed.PLAY_UPCOMING_SOURCE_TYPE}=$sourceType&${ApplinkConstInternalFeed.PLAY_UPCOMING_FILTER_CATEGORY}=$filterCategory"

        val newList = mutableListOf<PlayFeedUiModel>()
        for (item in itemList) {
            newList.add(item)
            if (item is PlaySlotTabMenuUiModel) break
        }
        if (slotPosition == null) slotPosition = newList.size - 1
        mappedData.forEach {  playFeedUiModel ->
            if (playFeedUiModel is PlayWidgetMediumUiModel) {
                val model: PlayWidgetUiModel = if (isUpcomingChannel(playFeedUiModel))
                    playFeedUiModel.model.copy(actionAppLink = feedPlayLehatSemuaApplink)
                else
                    playFeedUiModel.model.copy()
                val updatedItem = playFeedUiModel.copy(model = model)
                newList.add(updatedItem)
            } else {
                newList.add(playFeedUiModel)
            }

        }
        setItems(newList)
        notifyDataSetChanged()

    }

    fun isStickyHeaderView(it: Int): Boolean {
        return getItem(it) is PlaySlotTabMenuUiModel
    }


    fun getPositionInList(channelId: String, positionOfItem: Int): Int {
        itemList.forEachIndexed { index, playFeedUiModel ->
            if (playFeedUiModel is PlayWidgetMediumUiModel) {
                val item = playFeedUiModel.model.items[positionOfItem]
                if (item is PlayWidgetChannelUiModel) {
                    if (channelId == item.channelId)
                        return index
                }
            }
            if (playFeedUiModel is PlayWidgetJumboUiModel) {
                val item = playFeedUiModel.model.items[positionOfItem]
                if (item is PlayWidgetChannelUiModel) {
                    if (channelId == item.channelId)
                        return index
                }
            }
            if (playFeedUiModel is PlayWidgetLargeUiModel) {
                val item = playFeedUiModel.model.items[positionOfItem]
                if (item is PlayWidgetChannelUiModel) {
                    if (channelId == item.channelId)
                        return index
                }
            }

        }
        return -1
    }

    fun updateItemInList(position: Int, channelId: String, reminderType: PlayWidgetReminderType) {
        val list = mutableListOf<PlayFeedUiModel>()
        //update adapter list item at a particular position
        itemList.forEachIndexed { index, playFeedUiModel ->
            if (playFeedUiModel is PlayWidgetMediumUiModel && index == position) {
                val model = (itemList[position] as PlayWidgetMediumUiModel)
                val updatedItem = model.copy( model = updateWidgetActionReminder(model.model, channelId, reminderType) )
                list.add(updatedItem)
            } else if (playFeedUiModel is PlayWidgetJumboUiModel && index == position){
                val model = (itemList[position] as PlayWidgetJumboUiModel)
                val updatedItem = model.copy( model = updateWidgetActionReminder(model.model, channelId, reminderType) )
                list.add(updatedItem)
            } else if (playFeedUiModel is PlayWidgetLargeUiModel && index == position) {
                val model = (itemList[position] as PlayWidgetLargeUiModel)
                val updatedItem = model.copy(model = updateWidgetActionReminder(model.model, channelId, reminderType))
                list.add(updatedItem)
            } else {
                list.add(playFeedUiModel)
            }
        }
        setItems(list)
        notifyItemChanged(position)
    }

    private fun updateWidgetActionReminder(model: PlayWidgetUiModel, channelId: String, reminderType: PlayWidgetReminderType): PlayWidgetUiModel {
        return model.copy(
                items = model.items.map { mediumWidget ->
                    if (mediumWidget is PlayWidgetChannelUiModel && mediumWidget.channelId == channelId) mediumWidget.copy(reminderType = reminderType)
                    else mediumWidget
                }
        )
    }
    private fun isUpcomingChannel(playFeedUiModel: PlayWidgetMediumUiModel): Boolean {
        val channelList = playFeedUiModel.model.items
        if (channelList.isNotEmpty()){
            val firstChannelItem =  playFeedUiModel.model.items.first()
         return firstChannelItem is PlayWidgetChannelUiModel && firstChannelItem.channelType == PlayWidgetChannelType.Upcoming
        }

        return false

    }
}