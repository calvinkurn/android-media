package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.beranda.listener.HomeCategoryListener

/**
 * Created by frenzel
 */
abstract class BaseBalanceViewHolder<T>(v: View) : RecyclerView.ViewHolder(v) {
    abstract fun bind(model: T?, listener: HomeCategoryListener?)
}
