package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.HomeAbstractViewHolder

/**
 * Created by meta on 22/03/18.
 */

class EmptyBlankViewHolder(itemView: View) : HomeAbstractViewHolder<DynamicChannelViewModel>(itemView) {

    override fun bind(element: DynamicChannelViewModel) {

    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_blank
    }
}
