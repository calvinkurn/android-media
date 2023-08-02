package com.tokopedia.shop.score.stub.penalty.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shop.score.common.ShopScorePrefManager
import com.tokopedia.shop.score.penalty.di.scope.PenaltyScope
import com.tokopedia.shop.score.penalty.domain.old.mapper.PenaltyMapperOld
import com.tokopedia.shop.score.penalty.domain.old.usecase.GetShopPenaltyDetailMergeUseCaseOld
import com.tokopedia.shop.score.penalty.domain.old.usecase.GetShopPenaltyDetailUseCaseOld
import com.tokopedia.shop.score.stub.common.UserSessionStub
import com.tokopedia.shop.score.stub.common.graphql.repository.GraphqlRepositoryStub
import com.tokopedia.shop.score.stub.common.util.ShopScorePrefManagerStub
import com.tokopedia.shop.score.stub.penalty.domain.mapper.PenaltyMapperOldStub
import com.tokopedia.shop.score.stub.penalty.domain.usecase.GetShopPenaltyDetailMergeUseCaseOldStub
import com.tokopedia.shop.score.stub.penalty.domain.usecase.GetShopPenaltyDetailUseCaseOldStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [PenaltyViewModelModuleStub::class])
class PenaltyModuleStub {

    @PenaltyScope
    @Provides
    fun provideGetShopPenaltyDetailMergeUseCaseStub(
        graphqlRepositoryStub: GraphqlRepositoryStub,
        penaltyMapperOld: PenaltyMapperOld
    ): GetShopPenaltyDetailMergeUseCaseOld {
        return GetShopPenaltyDetailMergeUseCaseOldStub(graphqlRepositoryStub, penaltyMapperOld)
    }

    @PenaltyScope
    @Provides
    fun provideGetShopPenaltyDetailUseCaseStub(graphqlRepositoryStub: GraphqlRepositoryStub): GetShopPenaltyDetailUseCaseOld {
        return GetShopPenaltyDetailUseCaseOldStub(graphqlRepositoryStub)
    }

    @PenaltyScope
    @Provides
    fun provideUserSessionStub(@ApplicationContext context: Context): UserSessionInterface {
        return UserSessionStub(context)
    }

    @PenaltyScope
    @Provides
    fun providePenaltyMapper(@ApplicationContext context: Context): PenaltyMapperOld {
        return PenaltyMapperOldStub(context)
    }

    @PenaltyScope
    @Provides
    fun providePrefManager(@ApplicationContext context: Context): ShopScorePrefManager {
        return ShopScorePrefManagerStub(context)
    }
}
