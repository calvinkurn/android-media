package com.tokopedia.kol.feature.postdetail.view.adapter

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseAdapterDelegate
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.postdetail.view.adapter.viewholder.ContentDetailPostViewHolder

class ContentDetailPageRevampAdapter(
    dataSource: DataSource,
    contentDetailListener: ContentDetailPostViewHolder.CDPListener
): BaseDiffUtilAdapter<FeedXCard>(true)
{
    init {
        delegatesManager
            .addDelegate(CDPPostDelegate(dataSource, contentDetailListener))
    }

    override fun areItemsTheSame(oldItem: FeedXCard, newItem: FeedXCard): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FeedXCard, newItem: FeedXCard): Boolean {
        return oldItem == newItem
    }
    fun getList() = itemList


    private class CDPPostDelegate(
        private val dataSource: DataSource,
        private val contentDetailListener: ContentDetailPostViewHolder.CDPListener
    ) : BaseAdapterDelegate<FeedXCard,FeedXCard, ContentDetailPostViewHolder>(
        R.layout.item_cdp_revamp_view) {
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
                if (payloads.containsKey(IMAGE_ITEM_IMPRESSED) || payloads.containsKey(
                        VOD_ITEM_IMPRESSED)) {
                    holder.bindWithPayloads(item, payloads)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ContentDetailPostViewHolder {
            return ContentDetailPostViewHolder.create(parent, dataSource, contentDetailListener)
        }

        override fun isForViewType(
            itemList: List<FeedXCard>,
            position: Int,
            isFlexibleType: Boolean
        ): Boolean {
            return true
        }
    }
    companion object{
        private const val IMAGE_ITEM_IMPRESSED = "image_item_impressed"
        private const val VOD_ITEM_IMPRESSED = "vod_item_impressed"

    }

    interface DataSource {
        fun getData(): FeedXCard
        fun getPositionInFeed(): Int
    }
}