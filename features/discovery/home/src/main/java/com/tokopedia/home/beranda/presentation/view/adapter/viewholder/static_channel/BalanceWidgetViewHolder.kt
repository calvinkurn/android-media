package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.view.View
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.BalanceWidgetDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel

/**
 * Created by yfsx on 3/1/21.
 */

class BalanceWidgetViewHolder(itemView: View, val listener: HomeCategoryListener?) : AbstractViewHolder<BalanceWidgetDataModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_balance_widget
    }

    override fun bind(element: BalanceWidgetDataModel?) {
    }
}