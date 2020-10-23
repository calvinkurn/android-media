package com.tokopedia.updateinactivephone.revamp.di.module

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.updateinactivephone.revamp.di.InactivePhoneScope
import com.tokopedia.updateinactivephone.revamp.domain.data.AccountListDataModel
import com.tokopedia.updateinactivephone.revamp.domain.usecase.GetAccountListUseCase
import dagger.Module
import dagger.Provides

@Module
class InactivePhoneUseCaseModule {

    @InactivePhoneScope
    @Provides
    fun provideGraphqlUseCase(repository: GraphqlRepository): GraphqlUseCase<AccountListDataModel> {
        return GraphqlUseCase(repository)
    }

    @InactivePhoneScope
    @Provides
    fun provideGetAccountListUseCase(useCase: GraphqlUseCase<AccountListDataModel>): GetAccountListUseCase {
        return GetAccountListUseCase(useCase)
    }
}