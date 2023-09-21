package com.tokopedia.feedplus.browse.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedCategoryInspirationViewHolder
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedCategoryInspirationModel

/**
 * Created by kenny.hadisaputra on 21/09/23
 */
class FeedCategoryInspirationAdapter(
) : ListAdapter<FeedCategoryInspirationModel, RecyclerView.ViewHolder>(
        object : DiffUtil.ItemCallback<FeedCategoryInspirationModel>() {
            override fun areItemsTheSame(
                oldItem: FeedCategoryInspirationModel,
                newItem: FeedCategoryInspirationModel
            ): Boolean {
                return oldItem.modelId == newItem.modelId
            }

            override fun areContentsTheSame(
                oldItem: FeedCategoryInspirationModel,
                newItem: FeedCategoryInspirationModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CARD -> FeedCategoryInspirationViewHolder.Card.create(parent)
            TYPE_CHIPS -> FeedCategoryInspirationViewHolder.Chips.create(parent)
            else -> error("ViewHolder for $viewType is not supported")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            holder is FeedCategoryInspirationViewHolder.Card && item is FeedCategoryInspirationModel.Card -> {
                holder.bind(item)
            }
            holder is FeedCategoryInspirationViewHolder.Chips && item is FeedCategoryInspirationModel.Chips -> {
                holder.bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is FeedCategoryInspirationModel.Card -> TYPE_CARD
            is FeedCategoryInspirationModel.Chips -> TYPE_CHIPS
            else -> Int.MIN_VALUE
        }
    }

    fun getSpanSizeLookup(): GridLayoutManager.SpanSizeLookup {
        return object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val item = getItem(position)
                return when (item::class) {
                    FeedCategoryInspirationModel.Card::class -> 1
                    else -> 2
                }
            }
        }
    }

    companion object {
        private const val TYPE_CARD = 0
        private const val TYPE_CHIPS = 1
    }
}
