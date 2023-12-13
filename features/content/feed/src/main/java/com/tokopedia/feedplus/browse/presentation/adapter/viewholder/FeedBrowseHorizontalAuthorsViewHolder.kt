package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.feedplus.browse.data.model.AuthorWidgetModel
import com.tokopedia.feedplus.browse.presentation.adapter.AuthorAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.itemdecoration.FeedBrowseHorizontalChannelsItemDecoration
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseHorizontalAuthorsBinding

/**
 * Created by meyta.taliti on 16/11/23.
 */
internal class FeedBrowseHorizontalAuthorsViewHolder private constructor(
    private val binding: ItemFeedBrowseHorizontalAuthorsBinding,
    pool: RecycledViewPool,
    private val listener: Listener
) : RecyclerView.ViewHolder(binding.root) {

    private var mData: FeedBrowseItemListModel.HorizontalAuthors? = null

    private val adapter = AuthorAdapter(object : AuthorCardViewHolder.Item.Listener {
        override fun onWidgetImpressed(
            viewHolder: AuthorCardViewHolder.Item,
            item: AuthorWidgetModel
        ) {
            val data = mData ?: return
            listener.onWidgetImpressed(
                this@FeedBrowseHorizontalAuthorsViewHolder,
                data,
                item,
                viewHolder.absoluteAdapterPosition
            )
        }

        override fun onChannelClicked(
            viewHolder: AuthorCardViewHolder.Item,
            item: AuthorWidgetModel
        ) {
            val data = mData ?: return
            listener.onChannelClicked(
                this@FeedBrowseHorizontalAuthorsViewHolder,
                data,
                item,
                viewHolder.absoluteAdapterPosition
            )
        }

        override fun onAuthorClicked(
            viewHolder: AuthorCardViewHolder.Item,
            item: AuthorWidgetModel
        ) {
            val data = mData ?: return
            listener.onAuthorClicked(
                this@FeedBrowseHorizontalAuthorsViewHolder,
                data,
                item,
                viewHolder.absoluteAdapterPosition
            )
        }
    })

    init {
        binding.rvAuthors.adapter = adapter
        binding.rvAuthors.setRecycledViewPool(pool)
        binding.rvAuthors.setHasFixedSize(true)
        binding.rvAuthors.addItemDecoration(
            FeedBrowseHorizontalChannelsItemDecoration(binding.rvAuthors.resources)
        )
    }

    fun bind(item: FeedBrowseItemListModel.HorizontalAuthors) {
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
        ): FeedBrowseHorizontalAuthorsViewHolder {
            return FeedBrowseHorizontalAuthorsViewHolder(
                ItemFeedBrowseHorizontalAuthorsBinding.inflate(
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
            viewHolder: FeedBrowseHorizontalAuthorsViewHolder,
            widgetModel: FeedBrowseItemListModel.HorizontalAuthors,
            item: AuthorWidgetModel,
            authorWidgetPosition: Int
        )

        fun onChannelClicked(
            viewHolder: FeedBrowseHorizontalAuthorsViewHolder,
            widgetModel: FeedBrowseItemListModel.HorizontalAuthors,
            item: AuthorWidgetModel,
            authorWidgetPosition: Int
        )

        fun onAuthorClicked(
            viewHolder: FeedBrowseHorizontalAuthorsViewHolder,
            widgetModel: FeedBrowseItemListModel.HorizontalAuthors,
            item: AuthorWidgetModel,
            authorWidgetPosition: Int
        )

        companion object {
            val Default get() = object : Listener {
                override fun onWidgetImpressed(
                    viewHolder: FeedBrowseHorizontalAuthorsViewHolder,
                    widgetModel: FeedBrowseItemListModel.HorizontalAuthors,
                    item: AuthorWidgetModel,
                    authorWidgetPosition: Int
                ) {}

                override fun onChannelClicked(
                    viewHolder: FeedBrowseHorizontalAuthorsViewHolder,
                    widgetModel: FeedBrowseItemListModel.HorizontalAuthors,
                    item: AuthorWidgetModel,
                    authorWidgetPosition: Int
                ) {}

                override fun onAuthorClicked(
                    viewHolder: FeedBrowseHorizontalAuthorsViewHolder,
                    widgetModel: FeedBrowseItemListModel.HorizontalAuthors,
                    item: AuthorWidgetModel,
                    authorWidgetPosition: Int
                ) {}
            }
        }
    }
}
