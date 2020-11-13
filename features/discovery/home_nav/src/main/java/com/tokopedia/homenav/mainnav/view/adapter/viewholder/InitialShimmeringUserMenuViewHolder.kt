package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.viewmodel.InitialShimmerBuListDataModel
import com.tokopedia.homenav.mainnav.view.viewmodel.InitialShimmerMenuDataModel

class InitialShimmeringUserMenuViewHolder (itemView: View)
    : AbstractViewHolder<InitialShimmerMenuDataModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_nav_shimmer_usermenu
    }

    override fun bind(element: InitialShimmerMenuDataModel) {
    }
}