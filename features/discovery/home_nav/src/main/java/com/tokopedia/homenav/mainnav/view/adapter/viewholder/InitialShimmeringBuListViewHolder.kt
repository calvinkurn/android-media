package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.viewmodel.InitialShimmerBuListDataModel

class InitialShimmeringBuListViewHolder (itemView: View)
    : AbstractViewHolder<InitialShimmerBuListDataModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_nav_shimmer_bulist
    }

    override fun bind(element: InitialShimmerBuListDataModel) {
    }
}