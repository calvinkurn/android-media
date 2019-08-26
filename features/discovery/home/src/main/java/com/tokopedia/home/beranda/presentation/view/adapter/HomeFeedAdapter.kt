package com.tokopedia.home.beranda.presentation.view.adapter

import android.support.v7.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeFeedTypeFactory
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.HomeBannerFeedViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.HomeFeedViewHolder
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel

class HomeFeedAdapter : BaseListAdapter<Visitable<HomeFeedTypeFactory>, HomeFeedTypeFactory> {
    val layoutList = listOf(
            HomeFeedViewHolder.LAYOUT,
            HomeBannerFeedViewHolder.LAYOUT
    )

    constructor(baseListAdapterTypeFactory: HomeFeedTypeFactory) : super(baseListAdapterTypeFactory) {}

    constructor(baseListAdapterTypeFactory: HomeFeedTypeFactory, onAdapterInteractionListener: BaseListAdapter.OnAdapterInteractionListener<Visitable<HomeFeedTypeFactory>>) : super(baseListAdapterTypeFactory, onAdapterInteractionListener) {}

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        val layoutParams = holder.itemView.getLayoutParams() as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = getItemViewType(position) !in layoutList
        super.onBindViewHolder(holder, position)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int, payloads: List<Any>) {
        val layoutParams = holder.itemView.getLayoutParams() as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = getItemViewType(position) !in layoutList
        super.onBindViewHolder(holder, position, payloads)
    }
}
