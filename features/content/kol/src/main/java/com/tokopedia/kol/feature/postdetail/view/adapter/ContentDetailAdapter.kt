package com.tokopedia.kol.feature.postdetail.view.adapter

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseAdapterDelegate
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.postdetail.view.adapter.viewholder.ContentDetailPostViewHolder

class ContentDetailAdapter(
    ContentDetailListener: ContentDetailPostViewHolder.CDPListener
): BaseDiffUtilAdapter<FeedXCard>(true)
{
    init {
        delegatesManager
            .addDelegate(CDPPostDelegate(ContentDetailListener))
    }

    override fun areItemsTheSame(oldItem: FeedXCard, newItem: FeedXCard): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FeedXCard, newItem: FeedXCard): Boolean {
        return oldItem == newItem
    }
    fun getList() = itemList


    private class CDPPostDelegate(
        private val ContentDetailListener: ContentDetailPostViewHolder.CDPListener
    ) : BaseAdapterDelegate<FeedXCard,FeedXCard, ContentDetailPostViewHolder>(
        R.layout.item_content_detail_view) {
        override fun onBindViewHolder(item: FeedXCard, holder: ContentDetailPostViewHolder) {
            holder.bind(feedXCard = item)
        }

        override fun onBindViewHolderWithPayloads(
            item: FeedXCard,
            holder: ContentDetailPostViewHolder,
            payloads: Bundle
        ) {
            if (payloads.isEmpty) super.onBindViewHolderWithPayloads(item, holder, payloads)
            else {
                holder.bindWithPayloads(item, payloads)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ContentDetailPostViewHolder {
            return ContentDetailPostViewHolder.create(parent, ContentDetailListener)
        }

        override fun isForViewType(
            itemList: List<FeedXCard>,
            position: Int,
            isFlexibleType: Boolean
        ): Boolean {
            return true
        }
    }

}