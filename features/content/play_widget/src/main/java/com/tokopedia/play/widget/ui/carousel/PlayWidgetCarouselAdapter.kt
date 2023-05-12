package com.tokopedia.play.widget.ui.carousel

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.ext.isMuted

/**
 * Created by kenny.hadisaputra on 04/05/23
 */
class PlayWidgetCarouselAdapter(
    private val videoContentListener: PlayWidgetVideoContentViewHolder.Listener,
    private val upcomingListener: PlayWidgetUpcomingContentViewHolder.Listener,
) : ListAdapter<PlayWidgetChannelUiModel, RecyclerView.ViewHolder>(PlayWidgetCarouselDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_UPCOMING -> PlayWidgetUpcomingContentViewHolder.create(parent, upcomingListener)
            TYPE_VIDEO -> PlayWidgetVideoContentViewHolder.create(parent, videoContentListener)
            else -> error("View Type $viewType is not supported")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        when (holder) {
            is PlayWidgetVideoContentViewHolder -> holder.bind(data)
            is PlayWidgetUpcomingContentViewHolder -> holder.bind(data)
            else -> {}
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) onBindViewHolder(holder, position)
        else {
            val data = getItem(position)
            val payloadSet = payloads
                .filterIsInstance<PlayWidgetCarouselDiffCallback.Payloads>()
                .flatMap { it.payloads }
                .toSet()

            when (holder) {
                is PlayWidgetVideoContentViewHolder -> holder.bind(data, payloadSet)
                is PlayWidgetUpcomingContentViewHolder -> holder.bind(data, payloadSet)
                else -> {}
            }
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

class PlayWidgetCarouselDiffCallback : DiffUtil.ItemCallback<PlayWidgetChannelUiModel>() {
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

    override fun getChangePayload(
        oldItem: PlayWidgetChannelUiModel,
        newItem: PlayWidgetChannelUiModel
    ): Any? {
        val payloads = buildList {
            if (oldItem.reminderType != newItem.reminderType) {
                add(PAYLOAD_REMINDED_CHANGE)
            }
            if (oldItem.isMuted != newItem.isMuted) {
                add(PAYLOAD_MUTE_CHANGE)
            }
        }

        return if (payloads.isEmpty()) null else Payloads(payloads)
    }

    companion object {
        internal const val PAYLOAD_REMINDED_CHANGE = "reminded_change"
        internal const val PAYLOAD_MUTE_CHANGE = "mute_change"
    }

    data class Payloads(
        val payloads: List<String>,
    )
}
