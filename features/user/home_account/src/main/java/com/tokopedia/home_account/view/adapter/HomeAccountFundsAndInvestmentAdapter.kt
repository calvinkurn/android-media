package com.tokopedia.home_account.view.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.home_account.view.adapter.delegate.FundsAndInvestmentSubtitleDelegate
import com.tokopedia.home_account.view.adapter.delegate.FundsAndInvestmentTitleDelegate
import com.tokopedia.home_account.view.adapter.delegate.FundsAndInvestmentWalletDelegate
import com.tokopedia.home_account.view.adapter.uimodel.SubtitleUiModel
import com.tokopedia.home_account.view.adapter.uimodel.TitleUiModel
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

    fun findTitlePosition(): Int {
        val title = itemList.find { it is TitleUiModel}
        return if(title != null) {
            itemList.indexOf(title)
        } else {
            -1
        }
    }

    fun findSubtitlePosition(): Int {
        val title = itemList.find { it is SubtitleUiModel}
        return if(title != null) {
            itemList.indexOf(title)
        } else {
            -1
        }
    }

    fun addWalletItem(walletUiModel: WalletUiModel) {
        if (!walletUiModel.isNotConnected) {
            val subtitlePosition = findSubtitlePosition()
            if (findSubtitlePosition() > -1) {
                addItem(subtitlePosition, walletUiModel)
            } else {
                addItem(walletUiModel)
            }
        } else {
            val subtitlePosition = findSubtitlePosition()
            addItem(walletUiModel)
        }
    }
}