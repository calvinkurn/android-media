package com.tokopedia.homenav.mainnav.data.repository

import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.homenav.mainnav.data.pojo.MainNavPojo
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopInfoPojo
import com.tokopedia.homenav.mainnav.data.source.MainNavRemoteDataSource
import com.tokopedia.homenav.mainnav.data.usecase.GetCoroutineWalletBalanceUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainNavRepositoryImpl(
        private val mainNavRemoteDataSource: MainNavRemoteDataSource): MainNavRepo {

    override fun getMainNavData(shopId: Int): Flow<MainNavPojo?> = flow {
        this.emit(coroutineScope {
            val walletData = async { mainNavRemoteDataSource.getWalletData() }
            val membershipData = async { mainNavRemoteDataSource.getUserMembershipData() }
            val shopData = async { mainNavRemoteDataSource.getShopInfo(shopId) }

            val walletResult = try {
                walletData.await()
            } catch (e: Exception) {
                WalletBalanceModel()
            }

            val membershipResult = try {
                membershipData.await()
            } catch (e: Exception) {
                MembershipPojo()
            }

            val shopResult = try {
                shopData.await()
            } catch (e: Exception) {
                ShopInfoPojo()
            }

            val combinedData = MainNavPojo()
            walletResult?.let {
                combinedData.wallet = it
            }
            membershipResult?.let {
                combinedData.membership = membershipResult
            }
            shopResult?.let {
                combinedData.shop = shopResult
            }
            combinedData
        })
    }


}