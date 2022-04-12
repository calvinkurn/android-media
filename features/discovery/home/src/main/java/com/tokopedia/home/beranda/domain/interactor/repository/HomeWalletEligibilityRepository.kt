package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.navigation_common.model.wallet.WalletStatus
import com.tokopedia.navigation_common.usecase.GetWalletEligibilityUseCase
import javax.inject.Inject

class HomeWalletEligibilityRepository @Inject constructor(
        private val getWalletEligibilityUseCase: GetWalletEligibilityUseCase
): HomeRepository<WalletStatus> {
    override suspend fun getRemoteData(bundle: Bundle): WalletStatus {
        return getWalletEligibilityUseCase.executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): WalletStatus {
        return WalletStatus(

        )
    }
}