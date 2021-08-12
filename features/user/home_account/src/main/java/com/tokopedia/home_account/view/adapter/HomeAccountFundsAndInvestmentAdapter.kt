package com.tokopedia.home_account.view.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.home_account.view.adapter.delegate.FundsAndInvestmentSubtitleDelegate
import com.tokopedia.home_account.view.adapter.delegate.FundsAndInvestmentTitleDelegate
import com.tokopedia.home_account.view.adapter.delegate.FundsAndInvestmentWalletDelegate
import com.tokopedia.home_account.view.listener.WalletListener

class HomeAccountFundsAndInvestmentAdapter(
    walletListener: WalletListener,
) : BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(FundsAndInvestmentTitleDelegate())
        delegatesManager.addDelegate(FundsAndInvestmentWalletDelegate(walletListener))
        delegatesManager.addDelegate(FundsAndInvestmentSubtitleDelegate())
    }
}