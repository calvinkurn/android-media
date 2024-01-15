package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.feedplus.browse.data.model.StoryNodeModel
import com.tokopedia.feedplus.browse.presentation.adapter.StoryAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.itemdecoration.HorizontalStoriesItemDecoration
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseHorizontalStoriesBinding

/**
 * Created by meyta.taliti on 16/11/23.
 */
internal class HorizontalStoriesViewHolder private constructor(
    private val binding: ItemFeedBrowseHorizontalStoriesBinding,
    pool: RecycledViewPool,
    private val listener: Listener
) : RecyclerView.ViewHolder(binding.root) {

    private var mData: FeedBrowseItemListModel.HorizontalStories? = null

    private val adapter = StoryAdapter(object : StoryWidgetViewHolder.Item.Listener {
        override fun onWidgetImpressed(
            viewHolder: StoryWidgetViewHolder.Item,
            item: StoryNodeModel
        ) {
            val data = mData ?: return
            listener.onWidgetImpressed(this@HorizontalStoriesViewHolder, data, item, viewHolder.absoluteAdapterPosition)
        }

        override fun onWidgetClicked(viewHolder: StoryWidgetViewHolder.Item, item: StoryNodeModel) {
            val data = mData ?: return
            listener.onClicked(this@HorizontalStoriesViewHolder, data, item, viewHolder.absoluteAdapterPosition)
        }
    })

    init {
        binding.rvStories.adapter = adapter
        binding.rvStories.setRecycledViewPool(pool)
        binding.rvStories.setHasFixedSize(true)
        binding.rvStories.addItemDecoration(
            HorizontalStoriesItemDecoration(binding.rvStories.resources)
        )
    }

    fun bind(item: FeedBrowseItemListModel.HorizontalStories) {
        mData = item

        if (item.isLoading) {
            adapter.setLoading()
        } else {
            adapter.submitList(item.items)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            pool: RecycledViewPool,
            listener: Listener
        ): HorizontalStoriesViewHolder {
            return HorizontalStoriesViewHolder(
                ItemFeedBrowseHorizontalStoriesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                pool,
                listener
            )
        }
    }

    interface Listener {

        fun onWidgetImpressed(
            viewHolder: HorizontalStoriesViewHolder,
            widgetModel: FeedBrowseItemListModel.HorizontalStories,
            item: StoryNodeModel,
            storyWidgetPosition: Int
        )

        fun onClicked(
            viewHolder: HorizontalStoriesViewHolder,
            widgetModel: FeedBrowseItemListModel.HorizontalStories,
            item: StoryNodeModel,
            storyWidgetPosition: Int
        )

        companion object {
            val Default get() = object : Listener {
                override fun onWidgetImpressed(
                    viewHolder: HorizontalStoriesViewHolder,
                    widgetModel: FeedBrowseItemListModel.HorizontalStories,
                    item: StoryNodeModel,
                    storyWidgetPosition: Int
                ) {}

                override fun onClicked(
                    viewHolder: HorizontalStoriesViewHolder,
                    widgetModel: FeedBrowseItemListModel.HorizontalStories,
                    item: StoryNodeModel,
                    storyWidgetPosition: Int
                ) {}
            }
        }
    }
}
