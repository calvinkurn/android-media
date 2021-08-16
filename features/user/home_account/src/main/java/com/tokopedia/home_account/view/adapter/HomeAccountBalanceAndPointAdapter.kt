package com.tokopedia.home_account.view.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.home_account.view.adapter.delegate.HomeAccountBalanceAndPointDelegate
import com.tokopedia.home_account.view.adapter.delegate.HomeAccountBalanceAndPointShimmerDelegate
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointShimmerUiModel
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointUiModel
import com.tokopedia.home_account.view.listener.BalanceAndPointListener

class HomeAccountBalanceAndPointAdapter(
    balanceAndPointListener: BalanceAndPointListener,
) : BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(HomeAccountBalanceAndPointDelegate(balanceAndPointListener))
        delegatesManager.addDelegate(HomeAccountBalanceAndPointShimmerDelegate())
    }

    fun displayShimmer() {
        val list: MutableList<BalanceAndPointShimmerUiModel> = mutableListOf()
        for (i in 1..3) {
            list.add(BalanceAndPointShimmerUiModel(true))
        }
        clearAllItems()
        addItemsAndAnimateChanges(list)
    }

    fun hideShimmer(index: Int, item: BalanceAndPointUiModel) {
        val shimmerItem = getItem(index)
        if (shimmerItem is BalanceAndPointShimmerUiModel) {
            removeItemAt(index)
            addItem(index, item)
            notifyItemChanged(index)
        }
    }

    fun changeItemType(index: Int, type: Int) {
        val item = getItem(index)
        if (item is BalanceAndPointUiModel) {
            item.type = type
            notifyItemChanged(index)
        }
    }
}