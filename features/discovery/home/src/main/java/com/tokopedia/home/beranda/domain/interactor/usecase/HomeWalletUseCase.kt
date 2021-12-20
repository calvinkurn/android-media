package com.tokopedia.home.beranda.domain.interactor.usecase

interface HomeWalletUseCase {
    fun onGetWalletData()
    fun onGetTokopointData()
    fun onGetTokocashData()
    fun onGetTokocashPendingBalance()
}