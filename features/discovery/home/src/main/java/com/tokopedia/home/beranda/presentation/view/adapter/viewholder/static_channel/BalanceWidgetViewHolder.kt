package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeBalanceModel

/**
 * Created by yfsx on 3/1/21.
 */

class BalanceWidgetViewHolder(itemView: View, val listener: HomeCategoryListener?) : AbstractViewHolder<HomeBalanceModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_balance_widget
    }

    override fun bind(element: HomeBalanceModel?) {
    }
}