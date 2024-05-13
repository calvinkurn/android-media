package com.tokopedia.home_component.widget.balance

import androidx.recyclerview.widget.DiffUtil

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
