package com.tokopedia.home_account.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointUiModel
import com.tokopedia.home_account.view.adapter.viewholder.BalanceAndPointItemViewHolder
import com.tokopedia.home_account.view.adapter.viewholder.FundsAndInvestmentViewHolder
import com.tokopedia.home_account.view.listener.BalanceAndPointListener

class HomeAccountBalanceAndPointDelegate(private val balanceAndPointListener: BalanceAndPointListener) :
    TypedAdapterDelegate<BalanceAndPointUiModel, Any, BalanceAndPointItemViewHolder>(
        FundsAndInvestmentViewHolder.LAYOUT
    ) {

    override fun onBindViewHolder(item: BalanceAndPointUiModel, holder: BalanceAndPointItemViewHolder) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = true
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): BalanceAndPointItemViewHolder {
        return BalanceAndPointItemViewHolder(balanceAndPointListener, basicView)
    }
}