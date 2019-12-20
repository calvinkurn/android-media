package com.tokopedia.interest_pick_common.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.interest_pick_common.domain.query.GetInterestPickQuery
import com.tokopedia.interest_pick_common.domain.query.SubmitInterestPickQuery
import com.tokopedia.interest_pick_common.domain.usecase.GetInterestPickUseCase
import com.tokopedia.interest_pick_common.domain.usecase.SubmitInterestPickUseCase
import dagger.Module
import dagger.Provides

/**
 * @author by yoasfs on 2019-11-05
 */
@Module
class InterestPickCommonModule {

    @Provides
    fun provideMultiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase {
        return GraphqlInteractor.getInstance().multiRequestGraphqlUseCase
    }

    @Provides
    fun provideGetInterestPickUseCase(useCase: MultiRequestGraphqlUseCase): GetInterestPickUseCase {
        return GetInterestPickUseCase(GetInterestPickQuery.getQuery(), useCase)
    }

    @Provides
    fun provideSubmitInterestPickUseCase(useCase: MultiRequestGraphqlUseCase): SubmitInterestPickUseCase {
        return SubmitInterestPickUseCase(SubmitInterestPickQuery.getQuery(), useCase)
    }

}