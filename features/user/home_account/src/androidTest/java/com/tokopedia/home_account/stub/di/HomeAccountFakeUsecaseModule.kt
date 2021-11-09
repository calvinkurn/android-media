package com.tokopedia.home_account.stub.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_account.data.model.BalanceAndPointDataModel
import com.tokopedia.home_account.data.model.CentralizedUserAssetDataModel
import com.tokopedia.home_account.di.HomeAccountUserScope
import com.tokopedia.home_account.domain.usecase.*
import com.tokopedia.home_account.stub.domain.usecase.*
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.repository.TopAdsRepository
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class HomeAccountFakeUsecaseModule {

    @Provides
    @HomeAccountUserScope
    fun provideGetCentralizedUserAssetConfigUseCaseStub(
        graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatchers): GetCentralizedUserAssetConfigUseCaseStub {
        val usecase = GetCentralizedUserAssetConfigUseCaseStub(graphqlRepository, dispatcher)
//        usecase.response = centralizedUserAssetDataModel
        return usecase
    }

    @Provides
    @HomeAccountUserScope
    fun provideGetCentralizedUserAssetConfigUseCase(
        stub: GetCentralizedUserAssetConfigUseCaseStub
    ): GetCentralizedUserAssetConfigUseCase = stub

    @Provides
    @HomeAccountUserScope
    fun provideGetBalanceAndPointUseCaseStub(
        graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatchers): GetBalanceAndPointUseCaseStub {
        val usecase = GetBalanceAndPointUseCaseStub(graphqlRepository, dispatcher)
//        usecase.response = balanceAndPointDataModel
        return usecase
    }

    @Provides
    @HomeAccountUserScope
    fun provideGetBalanceAndPointUseCase(
        stub: GetBalanceAndPointUseCaseStub
    ): GetBalanceAndPointUseCase = stub

    @Provides
    @HomeAccountUserScope
    fun provideGetTokopointsBalanceAndPointUseCaseStub(
        graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatchers): GetTokopointsBalanceAndPointUseCaseStub {
        val usecase = GetTokopointsBalanceAndPointUseCaseStub(graphqlRepository, dispatcher)
//        usecase.response = balanceAndPointDataModel
        return usecase
    }

    @Provides
    @HomeAccountUserScope
    fun provideGetTokopointsBalanceAndPointUseCase(
        stub: GetTokopointsBalanceAndPointUseCaseStub
    ): GetTokopointsBalanceAndPointUseCase = stub

    @Provides
    @HomeAccountUserScope
    fun provideGetSaldoBalanceUseCaseStub(
        graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatchers): GetSaldoBalanceUseCaseStub {
        val usecase = GetSaldoBalanceUseCaseStub(graphqlRepository, dispatcher)
//        usecase.response = balanceAndPointDataModel
        return usecase
    }

    @Provides
    @HomeAccountUserScope
    fun provideGetSaldoBalanceUseCase(
        stub: GetSaldoBalanceUseCaseStub
    ): GetSaldoBalanceUseCase = stub

    @Provides
    @HomeAccountUserScope
    fun provideGetCoBrandCCBalanceUseCaseStub(
        graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatchers): GetCoBrandCCBalanceAndPointUseCaseStub {
        val usecase = GetCoBrandCCBalanceAndPointUseCaseStub(graphqlRepository, dispatcher)
//        usecase.response = balanceAndPointDataModel
        return usecase
    }

    @Provides
    @HomeAccountUserScope
    fun provideGetCoBrandCCBalanceAndPointUseCase(
        stub: GetCoBrandCCBalanceAndPointUseCaseStub
    ): GetCoBrandCCBalanceAndPointUseCase = stub

    @Provides
    @HomeAccountUserScope
    fun provideGetWalletEligibleUseCase(stub: GetWalletEligibleUseCaseStub): GetWalletEligibleUseCase {
        return stub
    }

    @Provides
    @HomeAccountUserScope
    fun provideHomeAccountShortUseCase(stub: HomeAccountShortcutUseCaseStub): HomeAccountShortcutUseCase {
        return stub
    }

    @Provides
    @HomeAccountUserScope
    fun provideHomeAccountUserUseCase(stub: HomeAccountUserUsecaseStub): HomeAccountUserUsecase {
        return stub
    }

    @Provides
    @HomeAccountUserScope
    fun provideSafeSettingProfileUseCase(stub: SafeSettingProfileUseCaseStub): SafeSettingProfileUseCase {
        return stub
    }

    @Provides
    fun provideTopAdsImageViewUseCase(userSession: UserSessionInterface): TopAdsImageViewUseCase {
        return TopAdsImageViewUseCase(userSession.userId, TopAdsRepository())
    }
}