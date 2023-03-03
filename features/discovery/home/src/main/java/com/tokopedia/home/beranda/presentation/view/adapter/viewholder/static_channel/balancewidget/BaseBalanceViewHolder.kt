package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel

abstract class BaseBalanceViewHolder(v: View): RecyclerView.ViewHolder(v) {
    abstract fun bind(drawerItem: BalanceDrawerItemModel?, listener: HomeCategoryListener?)
}
