package com.tokopedia.seller.menu.di.module

import com.tokopedia.seller.menu.common.domain.usecase.*
import com.tokopedia.seller.menu.di.scope.SellerMenuScope
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
@SellerMenuScope
class SellerMenuUseCaseModule {

    @SellerMenuScope
    @Provides
    fun provideGetAllShopInfoUseCase(
        userSession: UserSessionInterface,
        balanceInfoUseCase: BalanceInfoUseCase,
        getShopBadgeUseCase: GetShopBadgeUseCase,
        getShopTotalFollowersUseCase: GetShopTotalFollowersUseCase,
        shopStatusTypeUseCase: ShopStatusTypeUseCase,
        topAdsAutoTopupUseCase: TopAdsAutoTopupUseCase,
        topAdsDashboardDepositUseCase: TopAdsDashboardDepositUseCase
    ): GetAllShopInfoUseCase {
        return GetAllShopInfoUseCase(
            userSession,
            balanceInfoUseCase,
            getShopBadgeUseCase,
            getShopTotalFollowersUseCase,
            shopStatusTypeUseCase,
            topAdsAutoTopupUseCase,
            topAdsDashboardDepositUseCase
        )
    }
}