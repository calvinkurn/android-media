package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.viewmodel.InitialShimmerAccountDataModel

class InitialShimmeringAccountViewHolder (itemView: View)
    : AbstractViewHolder<InitialShimmerAccountDataModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_nav_shimmer_account
    }

    override fun bind(element: InitialShimmerAccountDataModel) {
    }
}