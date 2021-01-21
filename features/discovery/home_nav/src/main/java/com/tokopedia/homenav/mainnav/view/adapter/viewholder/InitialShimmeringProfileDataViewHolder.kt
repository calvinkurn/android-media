package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.datamodel.InitialShimmerProfileDataModel

class InitialShimmeringProfileDataViewHolder (itemView: View)
    : AbstractViewHolder<InitialShimmerProfileDataModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_nav_shimmer_profile_data
    }

    override fun bind(element: InitialShimmerProfileDataModel) {
    }
}