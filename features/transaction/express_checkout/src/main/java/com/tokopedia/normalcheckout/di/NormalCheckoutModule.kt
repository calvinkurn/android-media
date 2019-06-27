package com.tokopedia.normalcheckout.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.domain.GraphqlUseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@NormalCheckoutScope
@Module (includes = [NormalCheckoutUserModule::class])
class NormalCheckoutModule {

    @NormalCheckoutScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @NormalCheckoutScope
    @Provides
    @Named("Main")
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}