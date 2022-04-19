package com.tokopedia.home_account.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.view.adapter.uimodel.WalletShimmeringUiModel
import com.tokopedia.home_account.view.adapter.viewholder.FundsAndInvestmentShimmerViewHolder

class FundsAndInvestmentShimmerDelegate :
    TypedAdapterDelegate<WalletShimmeringUiModel, Any, FundsAndInvestmentShimmerViewHolder>(
        FundsAndInvestmentShimmerViewHolder.LAYOUT
    ) {

    override fun onBindViewHolder(item: WalletShimmeringUiModel, holder: FundsAndInvestmentShimmerViewHolder) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): FundsAndInvestmentShimmerViewHolder {
        return FundsAndInvestmentShimmerViewHolder(basicView)
    }
}