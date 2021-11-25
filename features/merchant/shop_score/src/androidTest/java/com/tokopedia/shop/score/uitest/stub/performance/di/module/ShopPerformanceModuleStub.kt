package com.tokopedia.shop.score.uitest.stub.performance.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gm.common.domain.interactor.GetShopInfoPeriodUseCase
import com.tokopedia.gm.common.domain.mapper.ShopScoreCommonMapper
import com.tokopedia.shop.score.common.ShopScorePrefManager
import com.tokopedia.shop.score.performance.di.scope.ShopPerformanceScope
import com.tokopedia.shop.score.performance.domain.usecase.GetShopPerformanceUseCase
import com.tokopedia.shop.score.uitest.stub.common.UserSessionStub
import com.tokopedia.shop.score.uitest.stub.common.graphql.repository.GraphqlRepositoryStub
import com.tokopedia.shop.score.uitest.stub.performance.domain.usecase.GetShopInfoPeriodUseCaseStub
import com.tokopedia.shop.score.uitest.stub.performance.domain.usecase.GetShopPerformanceUseCaseStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [ShopPerformanceViewModelModuleStub::class])
class ShopPerformanceModuleStub {

    @Provides
    @ShopPerformanceScope
    fun provideShopInfoPeriodUseCase(
        graphqlRepositoryStub: GraphqlRepositoryStub,
        shopScoreCommonMapper: ShopScoreCommonMapper
    ): GetShopInfoPeriodUseCase {
        return GetShopInfoPeriodUseCaseStub(graphqlRepositoryStub, shopScoreCommonMapper)
    }

    @Provides
    @ShopPerformanceScope
    fun provideGetShopPerformanceUseCase(graphqlRepositoryStub: GraphqlRepositoryStub): GetShopPerformanceUseCase {
        return GetShopPerformanceUseCaseStub(graphqlRepositoryStub)
    }

    @ShopPerformanceScope
    @Provides
    fun provideShopScorePrefsManager(@ApplicationContext context: Context): ShopScorePrefManager {
        return ShopScorePrefManager(context)
    }

    @ShopPerformanceScope
    @Provides
    fun provideUserSessionStub(@ApplicationContext context: Context): UserSessionInterface {
        return UserSessionStub(context)
    }
}