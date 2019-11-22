package com.tokopedia.loginregister.login.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.login.domain.pojo.StatusPinPojo
import dagger.Module
import dagger.Provides

/**
 * Created by Ade Fulki on 2019-10-09.
 * ade.hadian@tokopedia.com
 */

@LoginScope
@Module
class LoginUseCaseModule {

    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    fun provideStatusPinGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<StatusPinPojo> = GraphqlUseCase(graphqlRepository)

    @Provides
    fun provideRegisterCheckGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<RegisterCheckPojo> = GraphqlUseCase(graphqlRepository)
}