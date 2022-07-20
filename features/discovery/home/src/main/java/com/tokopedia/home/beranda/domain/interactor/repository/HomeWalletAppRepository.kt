package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.navigation_common.usecase.GetWalletAppBalanceUseCase
import com.tokopedia.navigation_common.usecase.pojo.walletapp.WalletAppData
import javax.inject.Inject

class HomeWalletAppRepository @Inject constructor(
        private val getWalletAppBalanceUseCase: GetWalletAppBalanceUseCase
): HomeRepository<WalletAppData> {
    override suspend fun getRemoteData(bundle: Bundle): WalletAppData {
        return getWalletAppBalanceUseCase.executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): WalletAppData {
        return WalletAppData(

        )
    }
}