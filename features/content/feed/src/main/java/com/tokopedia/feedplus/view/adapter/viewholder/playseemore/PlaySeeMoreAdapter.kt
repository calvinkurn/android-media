package com.tokopedia.feedplus.view.adapter.viewholder.playseemore

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.videoTabComponent.domain.delegate.PlayWidgetViewAdapterDelegate
import com.tokopedia.videoTabComponent.domain.model.data.PlayFeedUiModel
import com.tokopedia.videoTabComponent.domain.model.data.PlayWidgetLargeUiModel
import com.tokopedia.videoTabComponent.view.coordinator.PlayWidgetCoordinatorVideoTab

/**
 * Created by jegul on 07/10/20
 */
class PlaySeeMoreAdapter(
    coordinator: PlayWidgetCoordinatorVideoTab
) : BaseDiffUtilAdapter<PlayFeedUiModel>(isFlexibleType = true) {

    init {
        delegatesManager
                .addDelegate(PlayWidgetViewAdapterDelegate.Large(coordinator))
    }

    override fun areItemsTheSame(oldItem: PlayFeedUiModel, newItem: PlayFeedUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PlayFeedUiModel, newItem: PlayFeedUiModel): Boolean {
        return oldItem == newItem
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

        setItems(
            itemList.mapIndexed { index, playFeedUiModel ->
                if (playFeedUiModel is PlayWidgetLargeUiModel && index == position) {
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
}
