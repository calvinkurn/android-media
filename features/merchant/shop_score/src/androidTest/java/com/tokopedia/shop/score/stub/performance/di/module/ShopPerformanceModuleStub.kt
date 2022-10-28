package com.tokopedia.shop.score.stub.performance.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gm.common.domain.interactor.GetShopCreatedInfoUseCase
import com.tokopedia.gm.common.domain.mapper.ShopScoreCommonMapper
import com.tokopedia.shop.score.common.ShopScorePrefManager
import com.tokopedia.shop.score.performance.di.scope.ShopPerformanceScope
import com.tokopedia.shop.score.performance.domain.mapper.ShopScoreMapper
import com.tokopedia.shop.score.performance.domain.usecase.GetShopPerformanceUseCase
import com.tokopedia.shop.score.stub.common.UserSessionStub
import com.tokopedia.shop.score.stub.common.graphql.repository.GraphqlRepositoryStub
import com.tokopedia.shop.score.stub.common.util.ShopScorePrefManagerStub
import com.tokopedia.shop.score.stub.performance.domain.mapper.ShopScoreCommonMapperStub
import com.tokopedia.shop.score.stub.performance.domain.mapper.ShopScoreMapperStub
import com.tokopedia.shop.score.stub.performance.domain.usecase.GetShopCreatedInfoUseCaseStub
import com.tokopedia.shop.score.stub.performance.domain.usecase.GetShopPerformanceUseCaseStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [ShopPerformanceViewModelModuleStub::class])
class ShopPerformanceModuleStub {

    @ShopPerformanceScope
    @Provides
    fun provideShopInfoPeriodUseCaseStub(
        graphqlRepositoryStub: GraphqlRepositoryStub,
        shopScoreCommonMapper: ShopScoreCommonMapper
    ): GetShopCreatedInfoUseCase {
        return GetShopCreatedInfoUseCaseStub(graphqlRepositoryStub, shopScoreCommonMapper)
    }

    @ShopPerformanceScope
    @Provides
    fun provideGetShopPerformanceUseCaseStub(graphqlRepositoryStub: GraphqlRepositoryStub): GetShopPerformanceUseCase {
        return GetShopPerformanceUseCaseStub(graphqlRepositoryStub)
    }

    @ShopPerformanceScope
    @Provides
    fun provideShopScorePrefsManagerStub(@ApplicationContext context: Context): ShopScorePrefManager {
        return ShopScorePrefManagerStub(context)
    }

    @ShopPerformanceScope
    @Provides
    fun provideUserSessionStub(@ApplicationContext context: Context): UserSessionInterface {
        return UserSessionStub(context)
    }

    @ShopPerformanceScope
    @Provides
    fun provideShopScoreMapperStub(
        userSessionInterface: UserSessionInterface,
        @ApplicationContext context: Context,
        shopScorePrefManager: ShopScorePrefManager
    ): ShopScoreMapper {
        return ShopScoreMapperStub(userSessionInterface, context, shopScorePrefManager)
    }

    @ShopPerformanceScope
    @Provides
    fun provideShopScoreCommonMapperStub(): ShopScoreCommonMapper {
        return ShopScoreCommonMapperStub()
    }
}