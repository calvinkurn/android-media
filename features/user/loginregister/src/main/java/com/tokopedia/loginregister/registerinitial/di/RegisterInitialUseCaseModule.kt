package com.tokopedia.loginregister.registerinitial.di

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterRequestPojo
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterRequestV2
import dagger.Module
import dagger.Provides

/**
 * Created by Ade Fulki on 2019-10-18.
 * ade.hadian@tokopedia.com
 */

@Module
class RegisterInitialUseCaseModule {

    @Provides
    fun provideRegisterCheckGraphQlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<RegisterCheckPojo> =
        GraphqlUseCase(graphqlRepository)

    @Provides
    fun provideRegisterRequestGraphQlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<RegisterRequestPojo> =
        GraphqlUseCase(graphqlRepository)

    @Provides
    fun provideRegisterRequestV2GraphQlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<RegisterRequestV2> =
        GraphqlUseCase(graphqlRepository)
}
