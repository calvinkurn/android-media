package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelLoadingModel

/**
 * @author by DevAra on 02/04/20.
 */
class DynamicChannelLoadingViewHolder(itemView: View) : AbstractViewHolder<DynamicChannelLoadingModel?>(itemView) {
    override fun bind(element: DynamicChannelLoadingModel?) {

    }

    companion object {
        private val TAG = DynamicChannelLoadingViewHolder::class.java.simpleName

        @LayoutRes
        val LAYOUT = R.layout.home_loading_more_dynamic_channel
    }

}