package com.tokopedia.feedplus.view.adapter.viewholder.playseemore

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.videoTabComponent.callback.PlayWidgetCardClickListener
import com.tokopedia.videoTabComponent.domain.delegate.PlayWidgetViewAdapterDelegate
import com.tokopedia.videoTabComponent.domain.model.data.PlayFeedUiModel
import com.tokopedia.videoTabComponent.domain.model.data.PlayWidgetJumboUiModel
import com.tokopedia.videoTabComponent.domain.model.data.PlayWidgetLargeUiModel
import com.tokopedia.videoTabComponent.domain.model.data.PlayWidgetMediumUiModel
import com.tokopedia.videoTabComponent.view.coordinator.PlayWidgetCoordinatorVideoTab

/**
 * Created by jegul on 07/10/20
 */
class PlaySeeMoreAdapter(
    coordinator: PlayWidgetCoordinatorVideoTab,
    clickListener: PlayWidgetCardClickListener
) : BaseDiffUtilAdapter<PlayFeedUiModel>(isFlexibleType = true) {
    init {
        delegatesManager
                .addDelegate(PlayWidgetViewAdapterDelegate.Large(coordinator, clickListener))

    }

    override fun areItemsTheSame(oldItem: PlayFeedUiModel, newItem: PlayFeedUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PlayFeedUiModel, newItem: PlayFeedUiModel): Boolean {
        return oldItem == newItem
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
    }
    fun getPositionInList(channelId: String, positionOfItem: Int): Int {
        if(positionOfItem == -1) return -1

        itemList.forEachIndexed { index, playFeedUiModel ->
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

    fun updateReminderInList(position: Int, channelId: String, reminderType: PlayWidgetReminderType) {
        if(position == -1) return

        val list = mutableListOf<PlayFeedUiModel>()
        //update adapter list item at a particular position
        itemList.forEachIndexed { index, playFeedUiModel ->
            if (playFeedUiModel is PlayWidgetLargeUiModel && index == position) {
                val model = (itemList[position] as PlayWidgetLargeUiModel)
                val updatedItem = model.copy( model = updateWidgetActionReminder(model.model, channelId, reminderType) )
                list.add(updatedItem)
            } else {
                list.add(playFeedUiModel)
            }
        }
        setItems(list)
        notifyItemChanged(position)
    }

    fun updateTotalViewInList(position: Int, channelId: String, totalView: String) {
        if(position == -1) return

        val list = mutableListOf<PlayFeedUiModel>()
        itemList.forEachIndexed { index, playFeedUiModel ->
            if (playFeedUiModel is PlayWidgetLargeUiModel && index == position) {
                val model = (itemList[position] as PlayWidgetLargeUiModel)
                val updatedItem = model.copy(model = updateWidgetTotalView(model.model, channelId, totalView))
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
                items = model.items.map { largeWidget ->
                    if (largeWidget is PlayWidgetChannelUiModel && largeWidget.channelId == channelId) largeWidget.copy(reminderType = reminderType)
                    else largeWidget
                }
        )
    }

    private fun updateWidgetTotalView(model: PlayWidgetUiModel, channelId: String, totalView: String): PlayWidgetUiModel {
        return model.copy(
            items = model.items.map { widget ->
                if (widget is PlayWidgetChannelUiModel && widget.channelId == channelId)
                    widget.copy(totalView = widget.totalView.copy(totalViewFmt = totalView))
                else widget
            }
        )
    }
}