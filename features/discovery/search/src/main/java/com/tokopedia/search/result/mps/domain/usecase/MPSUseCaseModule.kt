package com.tokopedia.search.result.mps.domain.usecase

import com.tokopedia.discovery.common.constants.SearchConstant.MPS.MPS_USE_CASE
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class MPSUseCaseModule {

    @Provides
    @SearchScope
    @Named(MPS_USE_CASE)
    fun provideMPSFirstPageUseCase(): UseCase<MPSModel> {
        return MPSFirstPageUseCase(
            graphqlUseCase = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase
        )
    }
}
