package com.tokopedia.discovery.catalogrevamp.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.discovery.catalogrevamp.usecase.GetProductCatalogOneUseCase
import com.tokopedia.discovery.categoryrevamp.domain.usecase.*
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

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

    @CatalogScope
    @Named("topAdsProductListing")
    @Provides
    fun provideTopAdsUseCase(context: Context): TopAdsProductsUseCase {
        return TopAdsProductsUseCase(context)
    }

    @CatalogScope
    @Provides
    fun provideCategoryProductUseCase(context: Context, graphqlUseCase: GraphqlUseCase): CategoryProductUseCase {
        return CategoryProductUseCase(context, graphqlUseCase)
    }

    @CatalogScope
    @Provides
    fun provideQuickFilterUseCase(context: Context): QuickFilterUseCase {
        return QuickFilterUseCase(context)
    }

    @CatalogScope
    @Provides
    fun getProductListUseCase(categoryProductUseCase: CategoryProductUseCase
                              , @Named("topAdsProductListing") topAdsProductsUseCase
                              : TopAdsProductsUseCase)
            : GetProductListUseCase {
        return GetProductListUseCase(categoryProductUseCase, topAdsProductsUseCase)
    }

    @CatalogScope
    @Provides
    fun provideDynamicFilterUseCase(context: Context): DynamicFilterUseCase {
        return DynamicFilterUseCase(context)
    }

    @CatalogScope
    @Provides
    fun getAddWishListUseCase(context: Context)
            : AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @CatalogScope
    @Provides
    fun getRemoveWishListUseCase(context: Context)
            : RemoveWishListUseCase {
        return RemoveWishListUseCase(context)
    }

}
