package com.tokopedia.homenav.mainnav.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
import com.tokopedia.homenav.mainnav.data.factory.MainNavFactory
import com.tokopedia.homenav.mainnav.data.mapper.MainNavMapper
import com.tokopedia.homenav.mainnav.data.source.MainNavRemoteDataSource
import com.tokopedia.homenav.mainnav.data.usecase.GetCoroutineWalletBalanceUseCase
import com.tokopedia.homenav.mainnav.data.usecase.GetShopInfoUseCase
import com.tokopedia.homenav.mainnav.data.usecase.GetUserMembershipUseCase
import dagger.Module
import dagger.Provides

@Module
class MainNavDataSourceModule {

    @MainNavScope
    @Provides
    fun provideMainNavRemoteDataSource(
            dispatcher: NavDispatcherProvider,
            walletBalanceUseCase: GetCoroutineWalletBalanceUseCase,
            userMembershipUseCase: GetUserMembershipUseCase,
            shopInfoUseCase: GetShopInfoUseCase)
        = MainNavRemoteDataSource(
            dispatcher,
            walletBalanceUseCase,
            userMembershipUseCase,
            shopInfoUseCase)
}