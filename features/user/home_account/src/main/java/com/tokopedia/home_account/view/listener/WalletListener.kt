package com.tokopedia.home_account.view.listener

interface WalletListener {
    fun onClickWallet(id: String, applink: String?, weblink: String?, isFailed: Boolean)
}