package com.tokopedia.home_account.stub.di

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_account.data.model.BalanceAndPointDataModel
import com.tokopedia.home_account.data.model.CentralizedUserAssetDataModel
import com.tokopedia.home_account.di.HomeAccountUserScope
import com.tokopedia.home_account.domain.usecase.*
import com.tokopedia.home_account.stub.domain.usecase.*
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class HomeAccountFakeUsecaseModule {

    @Provides
    @HomeAccountUserScope
    fun provideGetCentralizedUserAssetConfigUseCaseStub(
        graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatchers,
        @Named("CentralizedUserAssetDataModelSuccess") centralizedUserAssetDataModel: CentralizedUserAssetDataModel
    ): GetCentralizedUserAssetConfigUseCaseStub {
        val usecase = GetCentralizedUserAssetConfigUseCaseStub(graphqlRepository, dispatcher)
        usecase.response = centralizedUserAssetDataModel
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
        dispatcher: CoroutineDispatchers,
        @Named("GopayBalanceAndPointDataModelSuccess") balanceAndPointDataModel: BalanceAndPointDataModel
    ): GetBalanceAndPointUseCaseStub {
        val usecase = GetBalanceAndPointUseCaseStub(graphqlRepository, dispatcher)
        usecase.response = balanceAndPointDataModel
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
        dispatcher: CoroutineDispatchers,
        @Named("TokopointBalanceAndPointDataModelSuccess") balanceAndPointDataModel: BalanceAndPointDataModel
    ): GetTokopointsBalanceAndPointUseCaseStub {
        val usecase = GetTokopointsBalanceAndPointUseCaseStub(graphqlRepository, dispatcher)
        usecase.response = balanceAndPointDataModel
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
        dispatcher: CoroutineDispatchers,
        @Named("SaldoBalanceAndPointDataModelSuccess") balanceAndPointDataModel: BalanceAndPointDataModel
    ): GetSaldoBalanceUseCaseStub {
        val usecase = GetSaldoBalanceUseCaseStub(graphqlRepository, dispatcher)
        usecase.response = balanceAndPointDataModel
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
        dispatcher: CoroutineDispatchers,
        @Named("CobrandCCBalanceAndPointDataModelSuccess") balanceAndPointDataModel: BalanceAndPointDataModel
    ): GetCoBrandCCBalanceAndPointUseCaseStub {
        val usecase = GetCoBrandCCBalanceAndPointUseCaseStub(graphqlRepository, dispatcher)
        usecase.response = balanceAndPointDataModel
        return usecase
    }

    @Provides
    @HomeAccountUserScope
    fun provideGetCoBrandCCBalanceAndPointUseCase(
        stub: GetCoBrandCCBalanceAndPointUseCaseStub
    ): GetCoBrandCCBalanceAndPointUseCase = stub
}