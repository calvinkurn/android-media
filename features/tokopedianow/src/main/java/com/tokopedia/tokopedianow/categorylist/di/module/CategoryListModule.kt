package com.tokopedia.tokopedianow.categorylist.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.categorylist.analytic.CategoryListAnalytics
import com.tokopedia.tokopedianow.categorylist.di.scope.CategoryListScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class CategoryListModule {

    @CategoryListScope
    @Provides
    fun provideCategoryAnalytic(): CategoryListAnalytics {
        return CategoryListAnalytics()
    }

    @CategoryListScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @CategoryListScope
    @Provides
    fun provideGrqphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}