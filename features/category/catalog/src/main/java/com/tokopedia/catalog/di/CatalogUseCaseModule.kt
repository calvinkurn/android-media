package com.tokopedia.catalog.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.catalog.repository.catalogdetail.CatalogDetailRepository
import com.tokopedia.common_category.usecase.*
import com.tokopedia.catalog.usecase.GetProductCatalogOneUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

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
    fun provideBaseRepository(): BaseRepository {
        return BaseRepository()
    }

    @CatalogScope
    @Provides
    fun provideCatalogDetailRepository(): CatalogDetailRepository {
        return CatalogDetailRepository()
    }

    @CatalogScope
    @Provides
    fun getProductCatalogOneUseCase(catalogDetailRepository: CatalogDetailRepository): GetProductCatalogOneUseCase {
        return GetProductCatalogOneUseCase(catalogDetailRepository)
    }

    @CatalogScope
    @Named("topAdsProductListing")
    @Provides
    fun provideTopAdsUseCase(): TopAdsProductsUseCase {
        return TopAdsProductsUseCase()
    }

    @CatalogScope
    @Provides
    fun provideCategoryProductUseCase(graphqlUseCase: GraphqlUseCase): CategoryProductUseCase {
        return CategoryProductUseCase(graphqlUseCase)
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
