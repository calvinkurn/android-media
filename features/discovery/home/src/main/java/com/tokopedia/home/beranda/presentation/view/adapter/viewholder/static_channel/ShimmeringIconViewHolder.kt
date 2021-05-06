package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.EmptyBannerDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ShimmeringChannelDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ShimmeringIconDataModel

class ShimmeringIconViewHolder(itemView: View, private val listener: HomeCategoryListener?)
: AbstractViewHolder<ShimmeringIconDataModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_inital_shimmer_icon_1_list
    }

    override fun bind(element: ShimmeringIconDataModel) {
    }
}