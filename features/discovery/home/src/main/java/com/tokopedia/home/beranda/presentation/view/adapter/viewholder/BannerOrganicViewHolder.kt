package com.tokopedia.home.beranda.presentation.view.adapter.viewholder

import android.support.annotation.LayoutRes
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder
import android.view.View
import com.tokopedia.home.R

class BannerOrganicViewHolder(
        itemView: View
): AbstractViewHolder<DynamicChannelViewModel>(itemView) {
    @LayoutRes
    val LAYOUT = R.layout.home_six_grid_channel
    override fun bind(element: DynamicChannelViewModel) {

    }
}