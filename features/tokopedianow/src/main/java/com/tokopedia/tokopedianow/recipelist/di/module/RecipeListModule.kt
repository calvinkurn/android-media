package com.tokopedia.tokopedianow.recipelist.di.module

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.recipelist.di.scope.RecipeListScope
import dagger.Module
import dagger.Provides

@Module
class RecipeListModule {

    @RecipeListScope
    @Provides
    fun provideGrqphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}