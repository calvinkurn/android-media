package com.tokopedia.phoneverification.di.revamp

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.phoneverification.data.model.response.PhoneVerificationResponseData
import dagger.Module
import dagger.Provides

@Module
class PhoneVerificationUseCaseModule {

    @PhoneVerificationScope
    @Provides
    fun provideGraphqlUseCase(repository: GraphqlRepository): GraphqlUseCase<PhoneVerificationResponseData> {
        return GraphqlUseCase(repository)
    }

    @PhoneVerificationScope
    @Provides
    fun provideGraphqlUseCasePhoneVerification(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<String> = GraphqlUseCase(graphqlRepository)
}