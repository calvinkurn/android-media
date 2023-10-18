package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.atf2

import androidx.recyclerview.widget.DiffUtil

/**
 * Created by frenzel
 */
class BalanceDiffCallback : DiffUtil.ItemCallback<BalanceVisitable>() {
    override fun areItemsTheSame(oldItem: BalanceVisitable, newItem: BalanceVisitable): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: BalanceVisitable, newItem: BalanceVisitable): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }
}
