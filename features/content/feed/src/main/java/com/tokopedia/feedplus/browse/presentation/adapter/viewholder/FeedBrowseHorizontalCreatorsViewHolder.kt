package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.data.model.AuthorWidgetModel
import com.tokopedia.feedplus.browse.presentation.adapter.CreatorAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.itemdecoration.FeedBrowseHorizontalChannelsItemDecoration
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseHorizontalCreatorsBinding

/**
 * Created by meyta.taliti on 16/11/23.
 */
internal class FeedBrowseHorizontalCreatorsViewHolder private constructor(
    private val binding: ItemFeedBrowseHorizontalCreatorsBinding,
    private val listener: Listener
) : RecyclerView.ViewHolder(binding.root) {

    private var mData: FeedBrowseItemListModel.HorizontalAuthors? = null

    private val adapter = CreatorAdapter(object : CreatorCardViewHolder.Item.Listener {
        override fun onChannelClicked(
            viewHolder: CreatorCardViewHolder.Item,
            item: AuthorWidgetModel
        ) {
            listener.onChannelClicked(this@FeedBrowseHorizontalCreatorsViewHolder, item)
        }

        override fun onAuthorClicked(
            viewHolder: CreatorCardViewHolder.Item,
            item: AuthorWidgetModel
        ) {
            val data = mData ?: return
            listener.onAuthorClicked(
                this@FeedBrowseHorizontalCreatorsViewHolder,
                data,
                item,
                viewHolder.absoluteAdapterPosition
            )
        }
    })

    init {
        binding.rvCreators.adapter = adapter
        binding.rvCreators.addItemDecoration(
            FeedBrowseHorizontalChannelsItemDecoration(binding.rvCreators.resources)
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
            listener: Listener
        ): FeedBrowseHorizontalCreatorsViewHolder {
            return FeedBrowseHorizontalCreatorsViewHolder(
                ItemFeedBrowseHorizontalCreatorsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener
            )
        }
    }

    interface Listener {

        fun onChannelClicked(
            viewHolder: FeedBrowseHorizontalCreatorsViewHolder,
            item: AuthorWidgetModel
        )

        fun onAuthorClicked(
            viewHolder: FeedBrowseHorizontalCreatorsViewHolder,
            widgetModel: FeedBrowseItemListModel.HorizontalAuthors,
            item: AuthorWidgetModel,
            authorWidgetPosition: Int
        )

        fun onRetry(
            viewHolder: FeedBrowseHorizontalCreatorsViewHolder,
            slotId: String
        )
    }
}
