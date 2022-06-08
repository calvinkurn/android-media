package com.tokopedia.feedplus.view.adapter.viewholder.playseemore

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.videoTabComponent.callback.PlayWidgetCardClickListener
import com.tokopedia.videoTabComponent.domain.delegate.PlayWidgetViewAdapterDelegate
import com.tokopedia.videoTabComponent.domain.model.data.PlayFeedUiModel
import com.tokopedia.videoTabComponent.domain.model.data.PlayWidgetLargeUiModel
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

    fun updatePlayWidgetInfo(
        position: Int,
        channelId: String,
        totalView: String?,
        isReminderSet: Boolean?
    ) {
        if(position == -1) return

        val list = mutableListOf<PlayFeedUiModel>()
        itemList.forEachIndexed { index, playFeedUiModel ->
            if (playFeedUiModel is PlayWidgetLargeUiModel && index == position) {
                val model = (itemList[position] as PlayWidgetLargeUiModel)
                val updatedItem = model.copy(model = updateChannelInfo(model.model, channelId, totalView, isReminderSet))
                list.add(updatedItem)
            } else {
                list.add(playFeedUiModel)
            }
        }
        setItems(list)
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
}