package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable

/**
 * Created by meta on 22/03/18.
 */

class EmptyBlankViewHolder(itemView: View) : AbstractViewHolder<HomeVisitable>(itemView) {

    override fun bind(element: HomeVisitable?) {
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_blank
    }
}
