package com.tokopedia.feedplus.browse.presentation.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.data.model.AuthorWidgetModel
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.AuthorCardViewHolder
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel.LoadingModel

/**
 * Created by meyta.taliti on 16/11/23.
 */
internal class AuthorAdapter(
    private val creatorListener: AuthorCardViewHolder.Item.Listener
) : ListAdapter<Any, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is AuthorWidgetModel && newItem is AuthorWidgetModel -> {
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
            TYPE_LOADING -> AuthorCardViewHolder.Placeholder.create(parent)
            TYPE_AUTHOR -> AuthorCardViewHolder.Item.create(parent, creatorListener)
            else -> error("No ViewHolder found for view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            holder is AuthorCardViewHolder.Item && item is AuthorWidgetModel -> {
                holder.bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = getItem(position)) {
            LoadingModel -> TYPE_LOADING
            is AuthorWidgetModel -> TYPE_AUTHOR
            else -> throw UnsupportedOperationException("Type of item $item is not supported")
        }
    }

    fun setLoading() {
        submitList(List(4) { LoadingModel })
    }

    companion object {
        private const val TYPE_LOADING = 0
        private const val TYPE_AUTHOR = 1
    }
}
