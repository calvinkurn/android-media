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

    fun showPlaceholderBalanceAndPoints(items: List<BalanceAndPointUiModel>) {
        clearAllItems()
        notifyDataSetChanged()
        addItemsAndAnimateChanges(items)
    }

    fun changeItemBySameId(balanceAndPointUiModel: BalanceAndPointUiModel) {
        val items = getItems().toMutableList()
        items.forEach {
            if (it is BalanceAndPointUiModel) {
                if (it.id == balanceAndPointUiModel.id) {
                    val position = items.indexOf(it)
                    removeItemAt(position)
                    addItem(position, balanceAndPointUiModel)
                    notifyItemChanged(position)
                }
            }
        }
    }

    fun changeItemTypeById(id: String, type: Int) {
        val items = getItems().toMutableList()
        items.forEach {
            if (it is BalanceAndPointUiModel) {
                if (it.id == id) {
                    val position = items.indexOf(it)
                    it.type = type
                    notifyItemChanged(position)
                }
            }
        }
    }
}