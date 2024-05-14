package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item.BalanceItemVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item.BalanceTypeFactory
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.widget.BalanceWidgetDiffCallback

/**
 * Created by frenzel
 */
class BalanceWidgetAdapter(
    private val balanceTypeFactory: BalanceTypeFactory
) : ListAdapter<BalanceItemVisitable, AbstractViewHolder<BalanceItemVisitable>>(BalanceWidgetDiffCallback()) {

    @Suppress("TooGenericExceptionCaught")
    fun setItemList(itemList: List<BalanceItemVisitable>) {
        try {
            submitList(itemList)
        } catch (_: Exception) { }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<BalanceItemVisitable> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return balanceTypeFactory.onCreateViewHolder(view, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type(balanceTypeFactory)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<BalanceItemVisitable>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: AbstractViewHolder<BalanceItemVisitable>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if(payloads.isEmpty()) onBindViewHolder(holder, position)
        else holder.bind(getItem(position), payloads)
    }
}
