package com.tokopedia.home.beranda.presentation.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeInitialShimmerDataModel

class HomeInitialShimmerViewHolder(itemView: View, private val listener: HomeCategoryListener?)
    : AbstractViewHolder<HomeInitialShimmerDataModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_home_initial_shimmer
    }

    override fun bind(element: HomeInitialShimmerDataModel) {
    }
}