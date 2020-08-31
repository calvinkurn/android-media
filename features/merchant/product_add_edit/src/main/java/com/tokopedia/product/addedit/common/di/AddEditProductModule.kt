package com.tokopedia.product.addedit.common.di

import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.common.coroutine.CoroutineDispatchers
import com.tokopedia.product.addedit.common.coroutine.CoroutineDispatchersProvider
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@AddEditProductScope
class AddEditProductModule {

    @AddEditProductScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @AddEditProductScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = Interactor.getInstance().graphqlRepository

    @AddEditProductScope
    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers = CoroutineDispatchersProvider
}