package com.tokopedia.feedplus.browse.presentation.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.common.producttag.view.adapter.viewholder.LoadingViewHolder
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.ChipsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseTitleViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.InspirationCardViewHolder
import com.tokopedia.feedplus.browse.presentation.model.CategoryInspirationMap
import com.tokopedia.feedplus.browse.presentation.model.ChipsModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.browse.presentation.model.LoadingModel
import com.tokopedia.feedplus.browse.presentation.model.SlotInfo
import com.tokopedia.feedplus.browse.presentation.model.isEmpty
import com.tokopedia.feedplus.browse.presentation.model.isLoading

/**
 * Created by kenny.hadisaputra on 30/10/23
 */
internal class CategoryInspirationAdapter(
    private val chipsListener: ChipsViewHolder.Listener,
    private val cardListener: InspirationCardViewHolder.Item.Listener
) : ListAdapter<Any, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(
            oldItem: Any,
            newItem: Any
        ): Boolean {
            return when {
                oldItem is FeedBrowseItemListModel.Chips.Item && newItem is FeedBrowseItemListModel.Chips.Item -> {
                    true
                }
                oldItem is FeedBrowseItemListModel.Title && newItem is FeedBrowseItemListModel.Title -> {
                    true
                }
                oldItem is FeedBrowseItemListModel.InspirationCard.Item && newItem is FeedBrowseItemListModel.InspirationCard.Item -> {
                    oldItem.item.channelId == newItem.item.channelId
                }
                else -> {
                    oldItem == newItem
                }
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: Any,
            newItem: Any
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CHIPS -> {
                ChipsViewHolder.create(parent, chipsListener)
            }
            TYPE_TITLE -> {
                FeedBrowseTitleViewHolder.create(parent)
            }
            TYPE_INSPIRATION_CARD -> {
                InspirationCardViewHolder.Item.create(parent, cardListener)
            }
            TYPE_INSPIRATION_CARD_PLACEHOLDER -> {
                InspirationCardViewHolder.Placeholder.create(parent)
            }
            TYPE_LOADING -> {
                LoadingViewHolder.create(parent)
            }
            else -> error("ViewType $viewType is not supported")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            holder is ChipsViewHolder && item is FeedBrowseItemListModel.Chips -> {
                holder.bind(item)
            }
            holder is FeedBrowseTitleViewHolder && item is FeedBrowseItemListModel.Title -> {
                holder.bind(item)
            }
            holder is InspirationCardViewHolder.Item && item is FeedBrowseItemListModel.InspirationCard.Item -> {
                holder.bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = getItem(position)) {
            is FeedBrowseItemListModel.Chips -> TYPE_CHIPS
            is FeedBrowseItemListModel.Title -> TYPE_TITLE
            is FeedBrowseItemListModel.InspirationCard.Item -> TYPE_INSPIRATION_CARD
            is FeedBrowseItemListModel.InspirationCard.Placeholder -> TYPE_INSPIRATION_CARD_PLACEHOLDER
            is LoadingModel -> TYPE_LOADING
            else -> error("Type $item is not supported in this page")
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        when (holder) {
            is InspirationCardViewHolder.Item -> holder.recycle()
        }
    }

    fun setList(
        state: ResultState,
        items: CategoryInspirationMap,
        selectedMenuId: String,
        onCommit: () -> Unit = {}
    ) {
        submitList(
            if (state.isLoading) getPlaceholders() else items.mapToListItems(state, selectedMenuId),
            onCommit
        )
    }

    private fun getPlaceholders(): List<FeedBrowseItemListModel> {
        return buildList {
            add(FeedBrowseItemListModel.Chips.Placeholder)
            addAll(List(6) { FeedBrowseItemListModel.InspirationCard.Placeholder })
        }
    }

    private fun CategoryInspirationMap.mapToListItems(
        pageState: ResultState,
        selectedMenuId: String
    ): List<Any> {
        return buildList {
            val isMenuEmpty = keys.isEmpty() || (keys.size == 1 && keys.first().isBlank())
            val selectedData = get(selectedMenuId) ?: values.firstOrNull()
            val menuItem = selectedData?.items ?: return@buildList

            if (!isMenuEmpty) {
                add(
                    FeedBrowseItemListModel.Chips.Item(
                        SlotInfo.Empty,
                        entries.map {
                            ChipsModel(it.value.menu, it.key == selectedMenuId)
                        }
                    )
                )
            }

            if (menuItem.isLoading && menuItem.isEmpty()) {
                addAll(List(6) { FeedBrowseItemListModel.InspirationCard.Placeholder })
            } else {
                addAll(
                    menuItem.items.map {
                        FeedBrowseItemListModel.InspirationCard.Item(SlotInfo.Empty, it)
                    }
                )
                if (menuItem.isLoading && !pageState.isLoading) { add(LoadingModel) }
            }
        }
    }

    fun getSpanSizeLookup(): GridLayoutManager.SpanSizeLookup {
        return object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (getItem(position)) {
                    is FeedBrowseItemListModel.Banner -> 1
                    is FeedBrowseItemListModel.InspirationCard -> 1
                    else -> 2
                }
            }
        }
    }

    companion object {
        private const val TYPE_CHIPS = 0
        private const val TYPE_TITLE = 1
        private const val TYPE_INSPIRATION_CARD = 2
        private const val TYPE_INSPIRATION_CARD_PLACEHOLDER = 3
        private const val TYPE_LOADING = 4
    }
}
