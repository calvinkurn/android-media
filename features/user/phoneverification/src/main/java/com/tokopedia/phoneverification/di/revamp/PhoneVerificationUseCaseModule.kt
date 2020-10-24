package com.tokopedia.phoneverification.di.revamp

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides

@Module
class PhoneVerificationUseCaseModule {

    
    @PhoneVerificationScope
    @Provides
    fun provideGraphqlUseCasePhoneVerification(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<String> = GraphqlUseCase(graphqlRepository)
}