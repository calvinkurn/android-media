package com.tokopedia.home_account.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointShimmerUiModel
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointUiModel
import com.tokopedia.home_account.view.adapter.viewholder.BalanceAndPointItemViewHolder
import com.tokopedia.home_account.view.adapter.viewholder.BalanceAndPointShimmerViewHolder
import com.tokopedia.home_account.view.adapter.viewholder.FundsAndInvestmentViewHolder
import com.tokopedia.home_account.view.listener.BalanceAndPointListener

class HomeAccountBalanceAndPointShimmerDelegate() :
    TypedAdapterDelegate<BalanceAndPointShimmerUiModel, Any, BalanceAndPointShimmerViewHolder>(
        BalanceAndPointShimmerViewHolder.LAYOUT
    ) {

    override fun onBindViewHolder(item: BalanceAndPointShimmerUiModel, holder: BalanceAndPointShimmerViewHolder) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): BalanceAndPointShimmerViewHolder {
        return BalanceAndPointShimmerViewHolder(basicView)
    }
}