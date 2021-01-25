package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.EmptyBannerDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ErrorStateChannelOneModel
import kotlinx.android.synthetic.main.home_error_state_channel_1.view.*

class ErrorStateChannelOneViewHolder(itemView: View, private val listener: HomeCategoryListener?)
: AbstractViewHolder<ErrorStateChannelOneModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_error_state_channel_1
    }

    override fun bind(element: ErrorStateChannelOneModel) {
        itemView.localload_error_state_channel.progressState = false

        itemView.localload_error_state_channel.refreshBtn?.setOnClickListener {
            listener?.refreshHomeData(forceRefresh = true)
            itemView.localload_error_state_channel.progressState = true
        }
    }
}