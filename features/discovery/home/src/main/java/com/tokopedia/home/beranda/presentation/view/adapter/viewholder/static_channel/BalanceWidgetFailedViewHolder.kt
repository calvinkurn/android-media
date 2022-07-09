package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceWidgetFailedModel

/**
 * Created by dhaba
 */
class BalanceWidgetFailedViewHolder (itemView: View, val listener: HomeCategoryListener?) :
    AbstractViewHolder<BalanceWidgetFailedModel>(itemView) {

    companion object {
        var LAYOUT = R.layout.layout_balance_widget_failed
    }

    override fun bind(element: BalanceWidgetFailedModel) {}
}