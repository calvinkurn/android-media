package com.tokopedia.feedplus.browse.presentation.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.data.model.StoryNodeModel
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.StoryWidgetViewHolder
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel.LoadingModel

internal class StoryAdapter(
    private val storyListener: StoryWidgetViewHolder.Item.Listener,
) : ListAdapter<Any, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is StoryNodeModel && newItem is StoryNodeModel -> {
                    oldItem.id == newItem.id
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
            TYPE_LOADING -> StoryWidgetViewHolder.Placeholder.create(parent)
            TYPE_STORY -> StoryWidgetViewHolder.Item.create(parent, storyListener)
            else -> error("No ViewHolder found for view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            holder is StoryWidgetViewHolder.Item && item is StoryNodeModel -> {
                holder.bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = getItem(position)) {
            LoadingModel -> TYPE_LOADING
            is StoryNodeModel -> TYPE_STORY
            else -> error("Type of item $item is not supported")
        }
    }

    fun setLoading() {
        submitList(List(6) { LoadingModel })
    }

    companion object {
        private const val TYPE_LOADING = 0
        private const val TYPE_STORY = 1
    }
}
