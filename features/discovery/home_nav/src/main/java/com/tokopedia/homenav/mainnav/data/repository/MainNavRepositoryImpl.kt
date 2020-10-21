package com.tokopedia.homenav.mainnav.data.repository

import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.homenav.mainnav.data.pojo.MainNavPojo
import com.tokopedia.homenav.mainnav.data.source.MainNavRemoteDataSource
import com.tokopedia.homenav.mainnav.data.usecase.GetCoroutineWalletBalanceUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainNavRepositoryImpl(
        private val mainNavRemoteDataSource: MainNavRemoteDataSource): MainNavRepo {

    override fun getMainNavData(): Flow<MainNavPojo?> = flow {
        coroutineScope {
            val walletData = async { mainNavRemoteDataSource.getWalletData() }

            val walletResult = try {
                walletData.await()
            } catch (e: Exception) {
                WalletBalanceModel()
            }

            val combinedData = MainNavPojo()
            walletResult?.let {
                combinedData.wallet = it
            }
            combinedData
        }
    }
}