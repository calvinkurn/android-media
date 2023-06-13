package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.atf2

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.BaseBalanceViewHolder
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.home.util.HomeServerLogger.TYPE_ERROR_SUBMIT_WALLET

/**
 * Created by frenzel
 */
class BalanceAtf2Adapter(
    val listener: HomeCategoryListener?,
    private val balanceTypeFactory: BalanceTypeFactory
) : ListAdapter<BalanceVisitable, BaseBalanceViewHolder<BalanceVisitable>>(BalanceDiffCallback()) {

    @Suppress("TooGenericExceptionCaught")
    fun setItemList(itemList: List<BalanceVisitable>) {
        try {
            submitList(itemList)
        } catch (e: Exception) {
            HomeServerLogger.logWarning(
                type = TYPE_ERROR_SUBMIT_WALLET,
                throwable = e,
                reason = e.message ?: ""
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBalanceViewHolder<BalanceVisitable> {
        return balanceTypeFactory.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type(balanceTypeFactory)
    }

    override fun onBindViewHolder(holder: BaseBalanceViewHolder<BalanceVisitable>, position: Int) {
        holder.bind(getItem(position), listener)
    }
}
