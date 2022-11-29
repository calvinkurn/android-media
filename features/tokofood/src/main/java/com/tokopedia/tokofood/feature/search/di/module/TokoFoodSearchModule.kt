package com.tokopedia.tokofood.feature.search.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokofood.feature.search.container.di.module.SearchContainerViewModelModule
import com.tokopedia.tokofood.feature.search.di.scope.TokoFoodSearchScope
import com.tokopedia.tokofood.feature.search.initialstate.di.module.InitialStateViewModelModule
import com.tokopedia.tokofood.feature.search.searchresult.di.module.SearchResultViewModelModule
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [
    SearchContainerViewModelModule::class,
    InitialStateViewModelModule::class,
    SearchResultViewModelModule::class
])
internal class TokoFoodSearchModule {

    @TokoFoodSearchScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface =
        UserSession(context)

    @Provides
    @TokoFoodSearchScope
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}