package com.tokopedia.discovery.catalogrevamp.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.discovery.catalogrevamp.usecase.GetProductCatalogOneUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import dagger.Module
import dagger.Provides

@CatalogScope
@Module
class CatalogUseCaseModule {

    @CatalogScope
    @Provides
    fun providesContexts(@ApplicationContext context: Context): Context {
        return context
    }

    @CatalogScope
    @Provides
    fun provideRxGQLUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @CatalogScope
    @Provides
    fun getProductCatalogOneUseCase(context: Context, graphqlUseCase: GraphqlUseCase): GetProductCatalogOneUseCase {
        return GetProductCatalogOneUseCase(context, graphqlUseCase)
    }

}