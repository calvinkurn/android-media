package com.tokopedia.home.beranda.data.usecase

interface HomeWalletUseCase {
    fun onGetWalletData()
    fun onGetTokopointData()
    fun onGetTokocashData()
    fun onGetTokocashPendingBalance()
}