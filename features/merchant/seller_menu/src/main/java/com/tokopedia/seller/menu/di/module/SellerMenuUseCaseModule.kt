package com.tokopedia.seller.menu.di.module

import com.tokopedia.seller.menu.common.domain.usecase.BalanceInfoUseCase
import com.tokopedia.seller.menu.common.domain.usecase.GetAllShopInfoUseCase
import com.tokopedia.seller.menu.common.domain.usecase.GetShopBadgeUseCase
import com.tokopedia.seller.menu.common.domain.usecase.GetShopTotalFollowersUseCase
import com.tokopedia.seller.menu.common.domain.usecase.ShopStatusTypeUseCase
import com.tokopedia.seller.menu.common.domain.usecase.TopAdsAutoTopupUseCase
import com.tokopedia.seller.menu.common.domain.usecase.TopAdsDashboardDepositUseCase
import com.tokopedia.seller.menu.di.scope.SellerMenuScope
import com.tokopedia.seller.menu.common.coroutine.SellerHomeCoroutineDispatcher
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
        topAdsDashboardDepositUseCase: TopAdsDashboardDepositUseCase,
        dispatcher: SellerHomeCoroutineDispatcher
    ): GetAllShopInfoUseCase {
        return GetAllShopInfoUseCase(
            userSession,
            balanceInfoUseCase,
            getShopBadgeUseCase,
            getShopTotalFollowersUseCase,
            shopStatusTypeUseCase,
            topAdsAutoTopupUseCase,
            topAdsDashboardDepositUseCase,
            dispatcher
        )
    }
}