package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import androidx.annotation.LayoutRes
import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelViewModel

/**
 * Created by meta on 22/03/18.
 */

class EmptyBlankViewHolder(itemView: View) : AbstractViewHolder<DynamicChannelViewModel>(itemView) {

    override fun bind(element: DynamicChannelViewModel) {

    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_blank
    }
}
