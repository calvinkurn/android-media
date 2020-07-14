package com.tokopedia.kategori.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kategori.usecase.AllCategoryQueryUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import dagger.Module
import dagger.Provides

@CategoryNavigationScope
@Module
class CategoryUseCaseModule {

    @CategoryNavigationScope
    @Provides
    fun providesContexts(@ApplicationContext context: Context): Context {
        return context
    }

    @CategoryNavigationScope
    @Provides
    fun provideRxGQLUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @CategoryNavigationScope
    @Provides
    fun getAllCategoryUseCase(context: Context, graphqlUseCase: GraphqlUseCase): AllCategoryQueryUseCase {
        return AllCategoryQueryUseCase(context, graphqlUseCase)
    }


}