package com.tokopedia.home.beranda.domain.interactor.usecase

interface HomeBalanceWidgetUseCase {
    suspend fun onGetWalletData()
    suspend fun onGetTokopointData()
    suspend fun onGetTokocashData()
    suspend fun onGetTokocashPendingBalance()
}