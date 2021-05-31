package com.tokopedia.kategori.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kategori.Constants
import com.tokopedia.kategori.R
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class CategoryUseCaseModule {

    @CategoryNavigationScope
    @Provides
    fun providesContexts(@ApplicationContext context: Context): Context {
        return context
    }

    @CategoryNavigationScope
    @Provides
    fun provideResources(context: Context): Resources {
        return context.resources
    }

    @CategoryNavigationScope
    @Provides
    fun provideRxGQLUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

}