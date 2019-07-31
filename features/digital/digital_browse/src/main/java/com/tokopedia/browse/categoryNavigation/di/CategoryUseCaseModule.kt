package com.tokopedia.browse.categoryNavigation.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.browse.categoryNavigation.domain.usecase.GetCategoryHotListUseCase
import com.tokopedia.browse.categoryNavigation.domain.usecase.GetCategoryLevelOneUseCase
import com.tokopedia.browse.categoryNavigation.domain.usecase.GetCategoryLevelTwoUsecase
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
    fun getCategoryListOneUseCase(context: Context, graphqlUseCase: GraphqlUseCase): GetCategoryLevelOneUseCase {
        return GetCategoryLevelOneUseCase(context, graphqlUseCase)
    }

    @CategoryNavigationScope
    @Provides
    fun getCategoryListTwoUseCase(context: Context, graphqlUseCase: GraphqlUseCase): GetCategoryLevelTwoUsecase {
        return GetCategoryLevelTwoUsecase(context, graphqlUseCase)
    }

    @CategoryNavigationScope
    @Provides
    fun getCategoryHotListUseCase(context: Context, graphqlUseCase: GraphqlUseCase): GetCategoryHotListUseCase {
        return GetCategoryHotListUseCase(context, graphqlUseCase)
    }


}