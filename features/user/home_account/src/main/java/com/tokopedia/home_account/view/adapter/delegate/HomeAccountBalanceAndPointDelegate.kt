package com.tokopedia.home_account.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointUiModel
import com.tokopedia.home_account.view.adapter.viewholder.BalanceAndPointItemViewHolder
import com.tokopedia.home_account.view.listener.BalanceAndPointListener

class HomeAccountBalanceAndPointDelegate(private val balanceAndPointListener: BalanceAndPointListener) :
    TypedAdapterDelegate<BalanceAndPointUiModel, Any, BalanceAndPointItemViewHolder>(
        BalanceAndPointItemViewHolder.LAYOUT
    ) {

    override fun onBindViewHolder(item: BalanceAndPointUiModel, holder: BalanceAndPointItemViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): BalanceAndPointItemViewHolder {
        return BalanceAndPointItemViewHolder(balanceAndPointListener, basicView)
    }
}