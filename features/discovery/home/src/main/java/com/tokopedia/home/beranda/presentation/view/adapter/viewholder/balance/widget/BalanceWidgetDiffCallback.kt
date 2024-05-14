package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.widget

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item.BalanceItemVisitable

/**
 * Created by frenzel
 */
class BalanceWidgetDiffCallback : DiffUtil.ItemCallback<BalanceItemVisitable>() {
    override fun areItemsTheSame(oldItem: BalanceItemVisitable, newItem: BalanceItemVisitable): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: BalanceItemVisitable, newItem: BalanceItemVisitable): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }
}
