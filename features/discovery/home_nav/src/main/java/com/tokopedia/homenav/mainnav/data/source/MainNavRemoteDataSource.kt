package com.tokopedia.homenav.mainnav.data.source

import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopInfoPojo
import com.tokopedia.homenav.mainnav.data.usecase.GetCoroutineWalletBalanceUseCase
import com.tokopedia.homenav.mainnav.data.usecase.GetShopInfoUseCase
import com.tokopedia.homenav.mainnav.data.usecase.GetUserMembershipUseCase
import kotlinx.coroutines.withContext

class MainNavRemoteDataSource(
        private val dispatcher: NavDispatcherProvider,
        private val walletBalanceUseCase: GetCoroutineWalletBalanceUseCase,
        private val getUserMembershipUseCase: GetUserMembershipUseCase,
        private val getShopInfoUseCase: GetShopInfoUseCase
) {

    suspend fun getWalletData(): WalletBalanceModel? = withContext(dispatcher.io()) {
        walletBalanceUseCase.executeOnBackground()
    }

    suspend fun getUserMembershipData(): MembershipPojo? = withContext(dispatcher.io()) {
        getUserMembershipUseCase.executeOnBackground()
    }

    suspend fun getShopInfo(shopId: Int): ShopInfoPojo = withContext(dispatcher.io()) {
        val params = GetShopInfoUseCase.createParam(partnerId = shopId)
        getShopInfoUseCase.params = params
        getShopInfoUseCase.executeOnBackground()
    }

}