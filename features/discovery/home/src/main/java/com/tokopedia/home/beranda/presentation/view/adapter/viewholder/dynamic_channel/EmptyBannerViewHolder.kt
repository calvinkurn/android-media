package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.EmptyBannerDataModel

class EmptyBannerViewHolder(itemView: View, private val listener: HomeCategoryListener?)
: AbstractViewHolder<EmptyBannerDataModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_banner_empty
    }

    override fun bind(element: EmptyBannerDataModel?) {
    }
}