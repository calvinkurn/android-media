package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.EmptyBannerDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ErrorStateChannelThreeModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ErrorStateChannelTwoModel

class ErrorStateChannelTwoViewHolder(itemView: View, private val listener: HomeCategoryListener?)
: AbstractViewHolder<ErrorStateChannelTwoModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_error_state_channel_2
    }

    override fun bind(element: ErrorStateChannelTwoModel) {
    }
}