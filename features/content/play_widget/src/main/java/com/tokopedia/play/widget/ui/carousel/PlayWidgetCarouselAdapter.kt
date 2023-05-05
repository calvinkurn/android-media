package com.tokopedia.play.widget.ui.carousel

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by kenny.hadisaputra on 04/05/23
 */
class PlayWidgetCarouselAdapter : ListAdapter<PlayWidgetChannelUiModel, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<PlayWidgetChannelUiModel>() {
        override fun areItemsTheSame(
            oldItem: PlayWidgetChannelUiModel,
            newItem: PlayWidgetChannelUiModel
        ): Boolean {
            return oldItem.channelId == newItem.channelId
        }

        override fun areContentsTheSame(
            oldItem: PlayWidgetChannelUiModel,
            newItem: PlayWidgetChannelUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return PlayWidgetLiveContentViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position % currentList.size)
        when (holder) {
            is PlayWidgetLiveContentViewHolder -> holder.bind(data)
        }
    }
}
