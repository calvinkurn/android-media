package com.tokopedia.catalog.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.catalog.domain.GetProductListFromSearchUseCase
import com.tokopedia.catalog.ui.mapper.CatalogDetailUiMapper
import com.tokopedia.oldcatalog.repository.CatalogAllReviewRepository
import com.tokopedia.oldcatalog.repository.CatalogComparisonProductRepository
import com.tokopedia.oldcatalog.repository.catalogdetail.CatalogDetailRepository
import com.tokopedia.oldcatalog.usecase.detail.CatalogAllReviewUseCase
import com.tokopedia.oldcatalog.usecase.detail.CatalogComparisonProductUseCase
import com.tokopedia.oldcatalog.usecase.detail.CatalogDetailUseCase
import com.tokopedia.oldcatalog.usecase.listing.CatalogCategoryProductUseCase
import com.tokopedia.oldcatalog.usecase.listing.CatalogDynamicFilterUseCase
import com.tokopedia.oldcatalog.usecase.listing.CatalogGetProductListUseCase
import com.tokopedia.oldcatalog.usecase.listing.CatalogQuickFilterUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import dagger.Module
import dagger.Provides

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
    fun provideCatalogAllReviewRepository(): CatalogAllReviewRepository {
        return CatalogAllReviewRepository()
    }

    @CatalogScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @CatalogScope
    @Provides
    fun provideCatalogComparisonProductRepository(): CatalogComparisonProductRepository {
        return CatalogComparisonProductRepository()
    }

    @CatalogScope
    @Provides
    fun getProductCatalogOneUseCase(
        catalogDetailRepository: CatalogDetailRepository,
        context: Context
    ): CatalogDetailUseCase {
        return CatalogDetailUseCase(catalogDetailRepository, CatalogDetailUiMapper(context))
    }

    @CatalogScope
    @Provides
    fun provideCatalogCategoryProductUseCase(graphqlUseCase: GraphqlUseCase): CatalogCategoryProductUseCase {
        return CatalogCategoryProductUseCase(graphqlUseCase)
    }

    @CatalogScope
    @Provides
    fun providesCatalogGetProductListUseCase(catalogCategoryProductUseCase: CatalogCategoryProductUseCase)
            : CatalogGetProductListUseCase {
        return CatalogGetProductListUseCase(catalogCategoryProductUseCase)
    }

    @CatalogScope
    @Provides
    fun provideCatalogDynamicFilterUseCase(): CatalogDynamicFilterUseCase {
        return CatalogDynamicFilterUseCase()
    }

    @CatalogScope
    @Provides
    fun provideCatalogQuickFilterUseCase(): CatalogQuickFilterUseCase {
        return CatalogQuickFilterUseCase()
    }

    @CatalogScope
    @Provides
    fun addWishListV2UseCase(@ApplicationContext graphqlRepository: GraphqlRepository): AddToWishlistV2UseCase {
        return AddToWishlistV2UseCase(graphqlRepository)
    }

    @CatalogScope
    @Provides
    fun deleteWishListV2UseCase(@ApplicationContext graphqlRepository: GraphqlRepository): DeleteWishlistV2UseCase {
        return DeleteWishlistV2UseCase(graphqlRepository)
    }

    @CatalogScope
    @Provides
    fun getCatalogAllReviewUseCase(catalogAllReviewRepository: CatalogAllReviewRepository): CatalogAllReviewUseCase {
        return CatalogAllReviewUseCase(catalogAllReviewRepository)
    }

    @CatalogScope
    @Provides
    fun getCatalogComparisonProductUseCase(catalogComparisonProductRepository: CatalogComparisonProductRepository): CatalogComparisonProductUseCase {
        return CatalogComparisonProductUseCase(catalogComparisonProductRepository)
    }

    @CatalogScope
    @Provides
    fun providesTrackingQueue(@ApplicationContext context: Context): TrackingQueue = TrackingQueue(context)

    @CatalogScope
    @Provides
    fun providesNewCatalogGetProductListUseCase(@ApplicationContext graphqlRepository: GraphqlRepository): GetProductListFromSearchUseCase {
        return GetProductListFromSearchUseCase(graphqlRepository)
    }
}
