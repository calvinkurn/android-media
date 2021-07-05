package com.tokopedia.seller.menu.di.module

import com.tokopedia.seller.menu.di.scope.SellerMenuScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.seller.menu.common.domain.usecase.*
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class SellerMenuUseCaseModule {

    @SellerMenuScope
    @Provides
    fun provideGetAllShopInfoUseCase(
            userSession: UserSessionInterface,
            balanceInfoUseCase: BalanceInfoUseCase,
            getShopBadgeUseCase: GetShopBadgeUseCase,
            getShopTotalFollowersUseCase: GetShopTotalFollowersUseCase,
            getUserShopInfoUseCase: GetUserShopInfoUseCase,
            topAdsAutoTopupUseCase: TopAdsAutoTopupUseCase,
            topAdsDashboardDepositUseCase: TopAdsDashboardDepositUseCase,
            dispatcher: CoroutineDispatchers
    ): GetAllShopInfoUseCase {
        return GetAllShopInfoUseCase(
                userSession,
                balanceInfoUseCase,
                getShopBadgeUseCase,
                getShopTotalFollowersUseCase,
                getUserShopInfoUseCase,
                topAdsAutoTopupUseCase,
                topAdsDashboardDepositUseCase,
                dispatcher
        )
    }
}