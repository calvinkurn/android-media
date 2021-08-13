package com.tokopedia.common_category.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides


@Module
class BaseCategoryNavUseCaseModule {

    @BaseCategoryNavScope
    @Provides
    fun providesContexts(@ApplicationContext context: Context): Context {
        return context
    }

    @BaseCategoryNavScope
    @Provides
    fun provideResources(context: Context): Resources {
        return context.resources
    }

    @BaseCategoryNavScope
    @Provides
    fun provideGraphQlRepo(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @BaseCategoryNavScope
    @Provides
    fun provideBaseRepository(): BaseRepository {
        return BaseRepository()
    }

    @BaseCategoryNavScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

}