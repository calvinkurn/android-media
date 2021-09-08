package com.tokopedia.home_account.view.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.home_account.view.adapter.delegate.FundsAndInvestmentSubtitleDelegate
import com.tokopedia.home_account.view.adapter.delegate.FundsAndInvestmentTitleDelegate
import com.tokopedia.home_account.view.adapter.delegate.FundsAndInvestmentWalletDelegate
import com.tokopedia.home_account.view.adapter.uimodel.WalletUiModel
import com.tokopedia.home_account.view.listener.WalletListener

class HomeAccountFundsAndInvestmentAdapter(
    walletListener: WalletListener,
) : BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(FundsAndInvestmentTitleDelegate())
        delegatesManager.addDelegate(FundsAndInvestmentWalletDelegate(walletListener))
        delegatesManager.addDelegate(FundsAndInvestmentSubtitleDelegate())
    }

    fun showPlaceholderFundsAndInvestments(items: List<WalletUiModel>) {
        clearAllItems()
        notifyDataSetChanged()
        addItemsAndAnimateChanges(items)
    }

    fun changeItemBySameId(walletUiModel: WalletUiModel) {
        val items = getItems().toMutableList()
        items.forEach {
            if (it is WalletUiModel) {
                if (it.id == walletUiModel.id) {
                    val position = items.indexOf(it)
                    removeItemAt(position)
                    addItem(position, walletUiModel)
                    notifyItemChanged(position)
                }
            }
        }
    }

    fun changeItemToFailed(id: String) {
        val items = getItems().toMutableList()
        items.forEach {
            if (it is WalletUiModel) {
                if (it.id == id) {
                    val position = items.indexOf(it)
                    it.isFailed = true
                    notifyItemChanged(position)
                }
            }
        }
    }
}