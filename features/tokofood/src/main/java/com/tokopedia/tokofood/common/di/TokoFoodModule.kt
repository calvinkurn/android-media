package com.tokopedia.tokofood.common.di

import androidx.lifecycle.SavedStateHandle
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides

@Module
class TokoFoodModule {

    @Provides
    @TokoFoodScope
    fun provideSavedStateHandle(): SavedStateHandle = SavedStateHandle()

    @Provides
    @TokoFoodScope
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

}