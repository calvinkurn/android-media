package com.tokopedia.feedplus.browse.presentation.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.ChipPlaceholderViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.ChipViewHolder
import com.tokopedia.feedplus.browse.presentation.model.ChipsModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel.LoadingModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
internal class FeedBrowseChipAdapter(
    private val listener: ChipViewHolder.Listener
) : ListAdapter<Any, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(
            oldItem: Any,
            newItem: Any
        ): Boolean {
            return when {
                oldItem is ChipsModel && newItem is ChipsModel -> oldItem.menu.id == newItem.menu.id
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

        override fun getChangePayload(
            oldItem: Any,
            newItem: Any
        ): Any? {
            val payloadBuilder = FeedBrowsePayloads.Builder()
            if (oldItem is ChipsModel && newItem is ChipsModel) {
                if (oldItem.isSelected != newItem.isSelected) payloadBuilder.addSelectedChipChanged()
            }
            return payloadBuilder.build()
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PLACEHOLDER -> ChipPlaceholderViewHolder.create(parent)
            TYPE_CHIPS -> ChipViewHolder.create(parent, listener)
            else -> error("View type $viewType is not supported")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            item is ChipsModel && holder is ChipViewHolder -> holder.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val type = getItem(position)) {
            is ChipsModel -> TYPE_CHIPS
            LoadingModel -> TYPE_PLACEHOLDER
            else -> error("Type $type is not supported")
        }
    }

    fun setLoading() {
        submitList(List(6) { LoadingModel })
    }

    companion object {
        private const val TYPE_PLACEHOLDER = 0
        private const val TYPE_CHIPS = 1
    }
}
