package com.tokopedia.home_account.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointShimmerUiModel
import com.tokopedia.home_account.view.adapter.viewholder.BalanceAndPointShimmerViewHolder

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
