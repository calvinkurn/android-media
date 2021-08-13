package com.tokopedia.loginregister.seamlesslogin.di

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.seamlesslogin.data.model.GenerateKeyPojo
import dagger.Module
import dagger.Provides

/**
 * Created by Ade Fulki on 2019-10-18.
 * ade.hadian@tokopedia.com
 */

@Module
class SeamlessLoginUseCaseModule{

    @Provides
    fun provideGetKeyUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<GenerateKeyPojo> = GraphqlUseCase(graphqlRepository)
}