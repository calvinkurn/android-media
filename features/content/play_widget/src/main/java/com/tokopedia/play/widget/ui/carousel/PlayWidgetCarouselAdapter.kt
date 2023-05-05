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
        return when (viewType) {
            TYPE_UPCOMING -> PlayWidgetUpcomingContentViewHolder.create(parent)
            TYPE_VIDEO -> PlayWidgetVideoContentViewHolder.create(parent)
            else -> error("View Type $viewType is not supported")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        when (holder) {
            is PlayWidgetVideoContentViewHolder -> holder.bind(data)
            is PlayWidgetUpcomingContentViewHolder -> holder.bind(data)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when {
            item.isUpcoming -> TYPE_UPCOMING
            else -> TYPE_VIDEO
        }
    }

    companion object {
        private const val TYPE_VIDEO = 0
        private const val TYPE_UPCOMING = 1
    }
}
