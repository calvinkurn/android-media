package com.tokopedia.discovery.categoryrevamp.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.discovery.categoryrevamp.domain.usecase.CatalogUseCase
import com.tokopedia.discovery.categoryrevamp.domain.usecase.CategoryProductUseCase
import com.tokopedia.discovery.categoryrevamp.domain.usecase.SubCategoryUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named


@CategoryNavScope
@Module
class CategoryNavUseCaseModule {

    @CategoryNavScope
    @Provides
    fun providesContexts(@ApplicationContext context: Context): Context {
        return context
    }

    @CategoryNavScope
    @Named("productGqlUseCaseObject")
    @Provides
    fun provideRxGQLProductUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @CategoryNavScope
    @Named("subCategoryGqlUseCaseObject")
    @Provides
    fun provideRxGQLSubCategoryUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @CategoryNavScope
    @Named("catalogGqlUseCase")
    @Provides
    fun providecatatlogUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @CategoryNavScope
    @Provides
    fun provideCategoryProductUseCase(context: Context,
                                      @Named("productGqlUseCaseObject") graphqlUseCase: GraphqlUseCase):
            CategoryProductUseCase {
        return CategoryProductUseCase(context, graphqlUseCase)
    }

    @CategoryNavScope
    @Provides
    fun provideSubCategoryList(context: Context, @Named("subCategoryGqlUseCaseObject") graphqlUseCase
    : GraphqlUseCase): SubCategoryUseCase {
        return SubCategoryUseCase(context, graphqlUseCase)
    }

    @CategoryNavScope
    @Provides
    fun provideCatatlogUseCase(context: Context, @Named("catalogGqlUseCase") graphqlUseCase
    : GraphqlUseCase): CatalogUseCase {
        return CatalogUseCase(context, graphqlUseCase)
    }


}