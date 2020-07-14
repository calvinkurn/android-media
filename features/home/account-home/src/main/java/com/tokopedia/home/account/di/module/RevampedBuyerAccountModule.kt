package com.tokopedia.home.account.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home.account.data.model.AccountModel
import com.tokopedia.home.account.di.scope.BuyerAccountScope
import com.tokopedia.navigation_common.model.SaldoModel
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class RevampedBuyerAccountModule {

    @BuyerAccountScope
    @Provides
    fun providesContext(@ApplicationContext context: Context): Context = context

    @BuyerAccountScope
    @Provides
    fun provideMainDispacther(): CoroutineDispatcher = Dispatchers.Main

    @BuyerAccountScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @BuyerAccountScope
    @Provides
    fun provideGraphqlUseCaseGetBuyerAccountData(graphqlRepository: GraphqlRepository): GraphqlUseCase<AccountModel> {
        return GraphqlUseCase(graphqlRepository)
    }

    @BuyerAccountScope
    @Provides
    fun provideGraphqlUseCaseGetUserSaldo(graphqlRepository: GraphqlRepository): GraphqlUseCase<SaldoModel> {
        return GraphqlUseCase(graphqlRepository)
    }
}