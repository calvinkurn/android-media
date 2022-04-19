package com.tokopedia.shop.score.stub.penalty.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shop.score.penalty.di.scope.PenaltyScope
import com.tokopedia.shop.score.penalty.domain.mapper.PenaltyMapper
import com.tokopedia.shop.score.penalty.domain.usecase.GetShopPenaltyDetailMergeUseCase
import com.tokopedia.shop.score.penalty.domain.usecase.GetShopPenaltyDetailUseCase
import com.tokopedia.shop.score.stub.common.UserSessionStub
import com.tokopedia.shop.score.stub.common.graphql.repository.GraphqlRepositoryStub
import com.tokopedia.shop.score.stub.penalty.domain.mapper.PenaltyMapperStub
import com.tokopedia.shop.score.stub.penalty.domain.usecase.GetShopPenaltyDetailMergeUseCaseStub
import com.tokopedia.shop.score.stub.penalty.domain.usecase.GetShopPenaltyDetailUseCaseStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [PenaltyViewModelModuleStub::class])
class PenaltyModuleStub {

    @PenaltyScope
    @Provides
    fun provideGetShopPenaltyDetailMergeUseCaseStub(
        graphqlRepositoryStub: GraphqlRepositoryStub,
        penaltyMapper: PenaltyMapper
    ): GetShopPenaltyDetailMergeUseCase {
        return GetShopPenaltyDetailMergeUseCaseStub(graphqlRepositoryStub, penaltyMapper)
    }

    @PenaltyScope
    @Provides
    fun provideGetShopPenaltyDetailUseCaseStub(graphqlRepositoryStub: GraphqlRepositoryStub): GetShopPenaltyDetailUseCase {
        return GetShopPenaltyDetailUseCaseStub(graphqlRepositoryStub)
    }

    @PenaltyScope
    @Provides
    fun provideUserSessionStub(@ApplicationContext context: Context): UserSessionInterface {
        return UserSessionStub(context)
    }

    @PenaltyScope
    @Provides
    fun providePenaltyMapper(@ApplicationContext context: Context): PenaltyMapper {
        return PenaltyMapperStub(context)
    }
}