package com.tokopedia.home_account.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.view.adapter.uimodel.WalletUiModel
import com.tokopedia.home_account.view.adapter.viewholder.FundsAndInvestmentViewHolder
import com.tokopedia.home_account.view.listener.WalletListener

class FundsAndInvestmentWalletDelegate(private val walletListener: WalletListener) :
    TypedAdapterDelegate<WalletUiModel, Any, FundsAndInvestmentViewHolder>(
        FundsAndInvestmentViewHolder.LAYOUT
    ) {

    override fun onBindViewHolder(item: WalletUiModel, holder: FundsAndInvestmentViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): FundsAndInvestmentViewHolder {
        return FundsAndInvestmentViewHolder(walletListener, basicView)
    }
}