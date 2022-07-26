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
    coordinator: PlayWidgetCoordinatorVideoTab,
    listener: PlaySlotTabCallback,
    activity: Activity,
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

    fun setCurrentHeader(currentHeader: Pair<Int, RecyclerView.ViewHolder>?) {
        mCurrentHeader = currentHeader
    }

    fun getCurrentHeader() = mCurrentHeader
    fun updateSlotPosition() {
        itemList.forEachIndexed { index, playFeedUiModel ->
            if (playFeedUiModel is PlaySlotTabMenuUiModel)
                slotPosition = index
        }
    }

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
        setItemsAndAnimateChanges(newList)

    }

    fun isStickyHeaderView(it: Int): Boolean {
        return getItem(it) is PlaySlotTabMenuUiModel
    }


    fun getPositionInList(channelId: String, positionOfItem: Int): Int {
        if(positionOfItem == -1) return -1

        itemList.forEachIndexed { index, playFeedUiModel ->
            if (playFeedUiModel is PlayWidgetMediumUiModel && playFeedUiModel.model.items.size > positionOfItem) {
                val item = playFeedUiModel.model.items[positionOfItem]
                if (item is PlayWidgetChannelUiModel) {
                    if (channelId == item.channelId)
                        return index
                }
            }
            if (playFeedUiModel is PlayWidgetJumboUiModel && playFeedUiModel.model.items.size > positionOfItem) {
                val item = playFeedUiModel.model.items[positionOfItem]
                if (item is PlayWidgetChannelUiModel) {
                    if (channelId == item.channelId)
                        return index
                }
            }
            if (playFeedUiModel is PlayWidgetLargeUiModel && playFeedUiModel.model.items.size > positionOfItem) {
                val item = playFeedUiModel.model.items[positionOfItem]
                if (item is PlayWidgetChannelUiModel) {
                    if (channelId == item.channelId)
                        return index
                }
            }

        }
        return -1
    }

    fun updatePlayWidgetInfo(
        position: Int,
        channelId: String,
        totalView: String?,
        isReminderSet: Boolean?,
    ) {
        if(position == -1) return

        setItems(
            itemList.mapIndexed { index, playFeedUiModel ->
                if (playFeedUiModel is PlayWidgetMediumUiModel && index == position) {
                    playFeedUiModel.copy(model = updateChannelInfo(playFeedUiModel.model, channelId, totalView, isReminderSet))
                } else if (playFeedUiModel is PlayWidgetJumboUiModel && index == position){
                    playFeedUiModel.copy(model = updateChannelInfo(playFeedUiModel.model, channelId, totalView, isReminderSet) )
                } else if (playFeedUiModel is PlayWidgetLargeUiModel && index == position) {
                    playFeedUiModel.copy(model = updateChannelInfo(playFeedUiModel.model, channelId, totalView, isReminderSet))
                } else {
                    playFeedUiModel
                }
            }
        )
        notifyItemChanged(position)
    }

    private fun updateChannelInfo(
        model: PlayWidgetUiModel,
        channelId: String,
        totalView: String?,
        isReminderSet: Boolean?
    ): PlayWidgetUiModel {
        return model.copy(
            items = model.items.map { widget ->
                if (widget is PlayWidgetChannelUiModel && widget.channelId == channelId) {
                    val finalTotalView = totalView ?: widget.totalView.totalViewFmt
                    val finalReminderType = when(isReminderSet) {
                        true -> PlayWidgetReminderType.Reminded
                        false -> PlayWidgetReminderType.NotReminded
                        null -> widget.reminderType
                    }

                    widget.copy(
                        reminderType = finalReminderType,
                        totalView = widget.totalView.copy(totalViewFmt = finalTotalView)
                    )
                }
                else widget
            }
        )
    }

    fun updateSlotTabViewHolderState() {
        slotPosition?.let {
            if (itemList[it] is PlaySlotTabMenuUiModel)
                notifyItemChanged(it)
        }
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