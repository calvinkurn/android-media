package com.tokopedia.play.widget.ui.widget.carousel

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.ext.isMuted

/**
 * Created by kenny.hadisaputra on 17/05/23
 */
internal class PlayWidgetCarouselAdapter(
    private val videoContentListener: PlayWidgetCarouselViewHolder.VideoContent.Listener,
    private val upcomingListener: PlayWidgetCarouselViewHolder.UpcomingContent.Listener
) : ListAdapter<PlayWidgetCarouselAdapter.Model, RecyclerView.ViewHolder>(PlayWidgetCarouselDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_UPCOMING -> PlayWidgetCarouselViewHolder.UpcomingContent.create(parent, upcomingListener)
            TYPE_VIDEO -> PlayWidgetCarouselViewHolder.VideoContent.create(
                parent,
                videoContentListener
            )
            else -> error("View Type $viewType is not supported")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        when (holder) {
            is PlayWidgetCarouselViewHolder.VideoContent -> holder.bind(data)
            is PlayWidgetCarouselViewHolder.UpcomingContent -> holder.bind(data)
            else -> {}
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            val data = getItem(position)
            val payloadSet = payloads
                .filterIsInstance<PlayWidgetCarouselDiffCallback.Payloads>()
                .flatMap { it.payloads }
                .toSet()

            when (holder) {
                is PlayWidgetCarouselViewHolder.VideoContent -> holder.bind(data, payloadSet)
                is PlayWidgetCarouselViewHolder.UpcomingContent -> holder.bind(data, payloadSet)
                else -> {}
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when {
            item.channel.isUpcoming -> TYPE_UPCOMING
            else -> TYPE_VIDEO
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        when (holder) {
            is PlayWidgetCarouselViewHolder.VideoContent -> holder.onRecycled()
        }
    }

    companion object {
        private const val TYPE_VIDEO = 0
        private const val TYPE_UPCOMING = 1
    }

    internal data class Model(
        val channel: PlayWidgetChannelUiModel,
        val isSelected: Boolean
    )
}

internal class PlayWidgetCarouselDiffCallback : DiffUtil.ItemCallback<PlayWidgetCarouselAdapter.Model>() {

    override fun areItemsTheSame(
        oldItem: PlayWidgetCarouselAdapter.Model,
        newItem: PlayWidgetCarouselAdapter.Model
    ): Boolean {
        return oldItem.channel.channelId == newItem.channel.channelId
    }

    override fun areContentsTheSame(
        oldItem: PlayWidgetCarouselAdapter.Model,
        newItem: PlayWidgetCarouselAdapter.Model
    ): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(
        oldItem: PlayWidgetCarouselAdapter.Model,
        newItem: PlayWidgetCarouselAdapter.Model
    ): Any? {
        val payloads = buildList {
            if (oldItem.channel.reminderType != newItem.channel.reminderType) {
                add(PAYLOAD_REMINDED_CHANGE)
            }
            if (oldItem.channel.isMuted != newItem.channel.isMuted) {
                add(PAYLOAD_MUTE_CHANGE)
            }
            if (oldItem.isSelected != newItem.isSelected) {
                add(PAYLOAD_SELECTED_CHANGE)
            }
            if (oldItem.channel.totalView.totalViewFmt != newItem.channel.totalView.totalViewFmt) {
                add(PAYLOAD_TOTAL_VIEW_CHANGE)
            }
        }

        return if (payloads.isEmpty()) null else Payloads(payloads)
    }

    companion object {
        internal const val PAYLOAD_REMINDED_CHANGE = "reminded_change"
        internal const val PAYLOAD_MUTE_CHANGE = "mute_change"
        internal const val PAYLOAD_SELECTED_CHANGE = "is_selected"
        internal const val PAYLOAD_TOTAL_VIEW_CHANGE = "total_view"
    }

    data class Payloads(
        val payloads: List<String>
    )
}
