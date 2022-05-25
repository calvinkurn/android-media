package com.tokopedia.usercomponents.explicit.di.module

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import dagger.Module
import dagger.Provides

@Module
class ExplicitModule {

    @ActivityScope
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

}