package com.tokopedia.home_account.view.listener

import com.tokopedia.home_account.view.adapter.uimodel.WalletUiModel

interface WalletListener {
    fun onClickWallet(walletUiModel: WalletUiModel)
}