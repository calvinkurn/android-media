package com.tokopedia.loginphone.chooseaccount.di

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginphone.chooseaccount.data.AccountListPojo
import dagger.Module
import dagger.Provides

/**
 * Created by Ade Fulki on 2019-11-18.
 * ade.hadian@tokopedia.com
 */

@Module
class ChooseAccountUseCaseModule{

    @Provides
    fun provideChooseAccountGraphQlUseCase(graphqlRepository: GraphqlRepository)
        : GraphqlUseCase<AccountListPojo> = GraphqlUseCase(graphqlRepository)
}