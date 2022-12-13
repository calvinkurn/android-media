package com.tokopedia.kyc_centralized.di.module

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kyc_centralized.di.UserIdentificationCommonModule
import com.tokopedia.kyc_centralized.domain.GetProjectInfoUseCase
import com.tokopedia.kyc_centralized.fakes.FakeGraphqlRepository

open class FakeUserIdentificationModule : UserIdentificationCommonModule() {

    override fun provideGraphQlRepository(): GraphqlRepository {
        return FakeGraphqlRepository()
    }

    override fun provideGetUserProjectInfoUseCase(
        repository: GraphqlRepository,
        dispatchers: CoroutineDispatchers,
    ): GetProjectInfoUseCase {
        return GetProjectInfoUseCase(FakeGraphqlRepository(), dispatchers)
    }
}
