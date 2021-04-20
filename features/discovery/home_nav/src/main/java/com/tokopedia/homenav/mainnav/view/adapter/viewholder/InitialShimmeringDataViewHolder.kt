package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.datamodel.InitialShimmerDataModel

class InitialShimmeringDataViewHolder (itemView: View)
    : AbstractViewHolder<InitialShimmerDataModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_nav_shimmer_bu_data
    }

    override fun bind(element: InitialShimmerDataModel) {
    }
}