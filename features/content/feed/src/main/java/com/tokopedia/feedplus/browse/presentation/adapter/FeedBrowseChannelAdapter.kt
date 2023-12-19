package com.tokopedia.feedplus.browse.presentation.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseChannelViewHolder
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel.LoadingModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
internal class FeedBrowseChannelAdapter(
    private val channelListener: FeedBrowseChannelViewHolder.Channel.Listener
) : ListAdapter<Any, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(
            oldItem: Any,
            newItem: Any
        ): Boolean {
            return when {
                oldItem is PlayWidgetChannelUiModel && newItem is PlayWidgetChannelUiModel -> {
                    oldItem.channelId == newItem.channelId
                }
                else -> oldItem == newItem
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: Any,
            newItem: Any
        ): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: Any, newItem: Any): Any? {
            val payloadsBuilder = FeedBrowsePayloads.Builder()
            if (oldItem is PlayWidgetChannelUiModel && newItem is PlayWidgetChannelUiModel) {
                if (oldItem.totalView != newItem.totalView) payloadsBuilder.addChannelItemTotalViewChanged()
            }
            return payloadsBuilder.build()
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_LOADING -> FeedBrowseChannelViewHolder.Loading.create(parent)
            TYPE_CHANNEL -> FeedBrowseChannelViewHolder.Channel.create(parent, channelListener)
            else -> error("No ViewHolder found for view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            holder is FeedBrowseChannelViewHolder.Channel && item is PlayWidgetChannelUiModel -> {
                holder.bind(item)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            return super.onBindViewHolder(holder, position, payloads)
        }
        val payload = payloads.filterIsInstance<FeedBrowsePayloads>().combine()
        val item = getItem(position)
        when {
            holder is FeedBrowseChannelViewHolder.Channel && item is PlayWidgetChannelUiModel -> {
                holder.bindPayloads(item, payload)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = getItem(position)) {
            LoadingModel -> TYPE_LOADING
            is PlayWidgetChannelUiModel -> TYPE_CHANNEL
            else -> error("Type of item $item is not supported")
        }
    }

    fun setLoading() {
        submitList(List(4) { LoadingModel })
    }

    companion object {
        private const val TYPE_LOADING = 0
        private const val TYPE_CHANNEL = 1
    }
}
