package com.tokopedia.homenav.mainnav.data.source

import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
import com.tokopedia.homenav.mainnav.data.usecase.GetCoroutineWalletBalanceUseCase
import kotlinx.coroutines.withContext

class MainNavRemoteDataSource(
        private val dispatcher: NavDispatcherProvider,
        private val walletBalanceUseCase: GetCoroutineWalletBalanceUseCase
) {

    suspend fun getWalletData(): WalletBalanceModel? = withContext(dispatcher.io()) {
        walletBalanceUseCase.executeOnBackground()
    }
}