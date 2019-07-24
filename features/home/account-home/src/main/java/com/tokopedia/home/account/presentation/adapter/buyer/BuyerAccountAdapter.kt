package com.tokopedia.home.account.presentation.adapter.buyer

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory
import com.tokopedia.home.account.presentation.viewholder.RecommendationProductViewHolder

/**
 * @author okasurya on 7/17/18.
 */
class BuyerAccountAdapter(adapterTypeFactory: AccountTypeFactory, visitables: List<Visitable<*>>) : BaseAdapter<AccountTypeFactory>(adapterTypeFactory, visitables) {
    var twoSpanLayout = listOf(
            RecommendationProductViewHolder.LAYOUT
    )

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        val layoutParams = holder.itemView.getLayoutParams() as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = getItemViewType(position) !in twoSpanLayout
        super.onBindViewHolder(holder, position)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int, payloads: List<Any>) {
        val layoutParams = holder.itemView.getLayoutParams() as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = getItemViewType(position) !in twoSpanLayout
        super.onBindViewHolder(holder, position, payloads)
    }
}
