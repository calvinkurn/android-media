package com.tokopedia.shop.score.stub.penalty.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shop.score.common.ShopScorePrefManager
import com.tokopedia.shop.score.common.analytics.ShopScorePenaltyTracking
import com.tokopedia.shop.score.penalty.di.scope.PenaltyScope
import com.tokopedia.shop.score.penalty.domain.mapper.PenaltyMapper
import com.tokopedia.shop.score.penalty.domain.usecase.GetNotYetDeductedPenaltyUseCase
import com.tokopedia.shop.score.penalty.domain.usecase.GetShopPenaltyDetailMergeUseCase
import com.tokopedia.shop.score.penalty.domain.usecase.GetShopPenaltyDetailUseCase
import com.tokopedia.shop.score.penalty.domain.usecase.ShopPenaltyTickerUseCase
import com.tokopedia.shop.score.stub.common.UserSessionStub
import com.tokopedia.shop.score.stub.common.graphql.repository.GraphqlRepositoryStub
import com.tokopedia.shop.score.stub.common.util.ShopScorePrefManagerStub
import com.tokopedia.shop.score.stub.penalty.domain.mapper.PenaltyMapperStub
import com.tokopedia.shop.score.stub.penalty.domain.usecase.GetNotYetDeductedPenaltyUseCaseStub
import com.tokopedia.shop.score.stub.penalty.domain.usecase.GetShopPenaltyDetailMergeUseCaseStub
import com.tokopedia.shop.score.stub.penalty.domain.usecase.GetShopPenaltyDetailUseCaseStub
import com.tokopedia.shop.score.stub.penalty.domain.usecase.ShopPenaltyTickerUseCaseStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [PenaltyViewModelModuleStub::class])
class PenaltyModuleStub {

    @PenaltyScope
    @Provides
    fun provideGetShopPenaltyDetailMergeUseCaseStub(
        graphqlRepositoryStub: GraphqlRepositoryStub
    ): GetShopPenaltyDetailMergeUseCase {
        return GetShopPenaltyDetailMergeUseCaseStub(graphqlRepositoryStub)
    }

    @PenaltyScope
    @Provides
    fun provideGetShopPenaltyDetailUseCaseStub(graphqlRepositoryStub: GraphqlRepositoryStub): GetShopPenaltyDetailUseCase {
        return GetShopPenaltyDetailUseCaseStub(graphqlRepositoryStub)
    }

    @PenaltyScope
    @Provides
    fun provideGetNotYetDeductedPenaltyUseCaseStub(graphqlRepositoryStub: GraphqlRepositoryStub): GetNotYetDeductedPenaltyUseCase {
        return GetNotYetDeductedPenaltyUseCaseStub(graphqlRepositoryStub)
    }

    @PenaltyScope
    @Provides
    fun provideShopPenaltyTickerUseCaseStub(graphqlRepositoryStub: GraphqlRepositoryStub): ShopPenaltyTickerUseCase {
        return ShopPenaltyTickerUseCaseStub(graphqlRepositoryStub)
    }

    @PenaltyScope
    @Provides
    fun provideUserSessionStub(@ApplicationContext context: Context): UserSessionInterface {
        return UserSessionStub(context)
    }

    @PenaltyScope
    @Provides
    fun providePenaltyMapper(
        @ApplicationContext context: Context,
        shopScorePrefManager: ShopScorePrefManager
    ): PenaltyMapper {
        return PenaltyMapperStub(context, shopScorePrefManager)
    }

    @PenaltyScope
    @Provides
    fun providePrefManager(@ApplicationContext context: Context): ShopScorePrefManager {
        return ShopScorePrefManagerStub(context)
    }

    @PenaltyScope
    @Provides
    fun provideShopScorePenaltyTracking(userSession: UserSessionInterface): ShopScorePenaltyTracking {
        return ShopScorePenaltyTracking(userSession)
    }
}
