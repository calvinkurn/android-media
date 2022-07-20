package com.tokopedia.kol.feature.postdetail.view.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseAdapterDelegate
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.kol.feature.postdetail.view.adapter.viewholder.CDPPostViewHolder
import com.tokopedia.kol.feature.postdetail.view.datamodel.CDPRevampDataUiModel

class CDPRevampAdapter(
    dataSource: DataSource,
    cdpListener: CDPPostViewHolder.CDPListener
): BaseDiffUtilAdapter<FeedXCard>(true)
{
    init {
        delegatesManager
            .addDelegate(CDPPostDelegate(dataSource, cdpListener))
    }

    override fun areItemsTheSame(oldItem: FeedXCard, newItem: FeedXCard): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FeedXCard, newItem: FeedXCard): Boolean {
        return oldItem == newItem
    }

    private class CDPPostDelegate(
        private val dataSource: DataSource,
        private val cdpListener: CDPPostViewHolder.CDPListener
    ) : BaseAdapterDelegate<FeedXCard,FeedXCard, CDPPostViewHolder>(
        com.tokopedia.feedcomponent.R.layout.item_dynamic_post_new
    ) {
        override fun onBindViewHolder(item: FeedXCard, holder: CDPPostViewHolder) {
            holder.bind()
        }

        override fun onCreateViewHolder(parent: ViewGroup, basicView: View): CDPPostViewHolder {
            return CDPPostViewHolder.create(parent, dataSource, cdpListener)
        }

        override fun isForViewType(
            itemList: List<FeedXCard>,
            position: Int,
            isFlexibleType: Boolean
        ): Boolean {
            TODO("Not yet implemented")
        }
    }

    interface DataSource {
        fun getData(): CDPRevampDataUiModel
        fun getPositionInFeed(): Int
    }
}