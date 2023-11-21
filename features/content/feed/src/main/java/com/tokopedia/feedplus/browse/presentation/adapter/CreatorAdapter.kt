package com.tokopedia.feedplus.browse.presentation.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.CreatorCardViewHolder
import com.tokopedia.feedplus.browse.presentation.model.LoadingModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by meyta.taliti on 16/11/23.
 */
internal class CreatorAdapter(
    private val creatorListener: CreatorCardViewHolder.Item.Listener
) : ListAdapter<Any, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is PlayWidgetChannelUiModel && newItem is PlayWidgetChannelUiModel -> {
                    oldItem.channelId == newItem.channelId
                }
                else -> oldItem == newItem
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }

    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_LOADING -> CreatorCardViewHolder.Placeholder.create(parent)
            TYPE_CREATOR -> CreatorCardViewHolder.Item.create(parent, creatorListener)
            else -> error("No ViewHolder found for view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            holder is CreatorCardViewHolder.Item && item is PlayWidgetChannelUiModel -> {
                holder.bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = getItem(position)) {
            LoadingModel -> TYPE_LOADING
            is PlayWidgetChannelUiModel -> TYPE_CREATOR
            else -> throw UnsupportedOperationException("Type of item $item is not supported")
        }
    }

    fun setLoading() {
        submitList(List(4) { LoadingModel })
    }

    companion object {
        private const val TYPE_LOADING = 0
        private const val TYPE_CREATOR = 1
    }
}
