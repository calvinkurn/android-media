package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TickerDataModel
import com.tokopedia.home.R

class TargetedTickerViewHolder(
    view: View,
    private val listener: HomeCategoryListener
) : AbstractViewHolder<TickerDataModel>(view) {

    override fun bind(element: TickerDataModel?) {

    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.layout_targeted_ticker
    }
}
