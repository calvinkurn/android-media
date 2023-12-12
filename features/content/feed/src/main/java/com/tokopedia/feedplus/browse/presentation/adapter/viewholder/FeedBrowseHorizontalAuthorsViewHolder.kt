package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
    private val listener: Listener,
) : RecyclerView.ViewHolder(binding.root) {

    private var mData: FeedBrowseItemListModel.HorizontalAuthors? = null

    private val adapter = AuthorAdapter(object : AuthorCardViewHolder.Item.Listener {
        override fun onChannelClicked(
            viewHolder: AuthorCardViewHolder.Item,
            item: AuthorWidgetModel
        ) {
            listener.onChannelClicked(this@FeedBrowseHorizontalAuthorsViewHolder, item)
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
                viewHolder.absoluteAdapterPosition,
            )
        }
    })

    init {
        binding.rvAuthors.adapter = adapter
        binding.rvAuthors.addItemDecoration(
            FeedBrowseHorizontalChannelsItemDecoration(binding.rvAuthors.resources)
        )
    }

    fun bind(item: FeedBrowseItemListModel.HorizontalAuthors) {
        mData = item

        if (item.isLoading) adapter.setLoading()
        else adapter.submitList(item.items)
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: Listener,
        ) : FeedBrowseHorizontalAuthorsViewHolder {
            return FeedBrowseHorizontalAuthorsViewHolder(
                ItemFeedBrowseHorizontalAuthorsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener,
            )
        }
    }

    interface Listener {

        fun onChannelClicked(
            viewHolder: FeedBrowseHorizontalAuthorsViewHolder,
            item: AuthorWidgetModel
        )

        fun onAuthorClicked(
            viewHolder: FeedBrowseHorizontalAuthorsViewHolder,
            widgetModel: FeedBrowseItemListModel.HorizontalAuthors,
            item: AuthorWidgetModel,
            authorWidgetPosition: Int,
        )

        fun onRetry(
            viewHolder: FeedBrowseHorizontalAuthorsViewHolder,
            slotId: String
        )
    }
}
