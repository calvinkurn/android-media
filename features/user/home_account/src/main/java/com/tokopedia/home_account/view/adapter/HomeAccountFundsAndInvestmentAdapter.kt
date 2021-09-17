package com.tokopedia.home_account.view.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.home_account.view.adapter.delegate.FundsAndInvestmentShimmerDelegate
import com.tokopedia.home_account.view.adapter.delegate.FundsAndInvestmentSubtitleDelegate
import com.tokopedia.home_account.view.adapter.delegate.FundsAndInvestmentTitleDelegate
import com.tokopedia.home_account.view.adapter.delegate.FundsAndInvestmentWalletDelegate
import com.tokopedia.home_account.view.adapter.uimodel.WalletShimmeringUiModel
import com.tokopedia.home_account.view.adapter.uimodel.WalletUiModel
import com.tokopedia.home_account.view.listener.WalletListener
import com.tokopedia.home_account.view.mapper.UiModelMapper

class HomeAccountFundsAndInvestmentAdapter(
    walletListener: WalletListener,
) : BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(FundsAndInvestmentShimmerDelegate())
        delegatesManager.addDelegate(FundsAndInvestmentTitleDelegate())
        delegatesManager.addDelegate(FundsAndInvestmentWalletDelegate(walletListener))
        delegatesManager.addDelegate(FundsAndInvestmentSubtitleDelegate())
    }

    fun addShimmeringItemView(walletShimmeringUiModel: WalletShimmeringUiModel) {
        addItem(walletShimmeringUiModel)
    }

    fun showPlaceholderFundsAndInvestments(items: List<WalletUiModel>) {
        clearAllItems()
        notifyDataSetChanged()
        addItemsAndAnimateChanges(items)
    }

    fun changeItemBySameId(walletUiModel: WalletUiModel) {
        val items = getItems().toMutableList()
        items.forEach {
            when (it) {
                is WalletUiModel -> {
                    if (it.id == walletUiModel.id) {
                        val position = items.indexOf(it)
                        removeItemAt(position)
                        walletUiModel.title = it.title
                        addItem(position, walletUiModel)
                        notifyItemChanged(position)
                    }
                }

                is WalletShimmeringUiModel -> {
                    if (it.id == walletUiModel.id) {
                        walletUiModel.title = it.title
                        val position = items.indexOf(it)
                        removeItemAt(position)
                        addItem(position, walletUiModel)
                        notifyItemChanged(position)
                    }
                }
            }
        }
    }

    fun changeItemToFailed(id: String) {
        val items = getItems().toMutableList()
        items.forEach {
            when (it) {
                is WalletUiModel -> {
                    if (it.id == id) {
                        val position = items.indexOf(it)
                        it.isFailed = true
                        notifyItemChanged(position)
                    }
                }

                is WalletShimmeringUiModel -> {
                    if (it.id == id) {
                        val position = items.indexOf(it)
                        removeItemAt(position)
                        addItem(position, UiModelMapper.getDefaultAssetConfigUiModel(it))
                        notifyItemChanged(position)
                    }
                }
            }
        }
    }

    fun removeById(id: String) {
        val items = getItems().toMutableList()
        items.forEach {
            when (it) {
                is WalletUiModel -> {
                    if (it.id == id) {
                        val position = items.indexOf(it)
                        removeItemAt(position)
                        notifyItemRemoved(position)
                    }
                }

                is WalletShimmeringUiModel -> {
                    if (it.id == id) {
                        val position = items.indexOf(it)
                        removeItemAt(position)
                        notifyItemRemoved(position)
                    }
                }
            }
        }
    }
}