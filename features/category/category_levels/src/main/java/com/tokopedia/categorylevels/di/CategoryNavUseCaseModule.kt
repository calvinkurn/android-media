package com.tokopedia.categorylevels.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.categorylevels.domain.usecase.SubCategoryV3UseCase
import com.tokopedia.categorylevels.view.activity.CategoryNavActivity
import com.tokopedia.common_category.usecase.*
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
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
    @Named("subCategoryV3GqlUseCaseObject")
    @Provides
    fun provideGqlUseCaseForSubV3(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @CategoryNavScope
    @Named("catalogGqlUseCase")
    @Provides
    fun providecatalogGqlUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @CategoryNavScope
    @Provides
    fun provideCategoryProductUseCase(context: Context,
                                      @Named("productGqlUseCaseObject") graphqlUseCase: GraphqlUseCase):
            CategoryProductUseCase {
        return CategoryProductUseCase(graphqlUseCase)
    }

    @CategoryNavScope
    @Named("topAdsProductListing")
    @Provides
    fun provideTopAdsUseCase(): TopAdsProductsUseCase {
        return TopAdsProductsUseCase()
    }


    @CategoryNavScope
    @Provides
    fun getProductListUseCase(categoryProductUseCase: CategoryProductUseCase
                              , @Named("topAdsProductListing") topAdsProductsUseCase
                              : TopAdsProductsUseCase)
            : GetProductListUseCase {
        return GetProductListUseCase(categoryProductUseCase, topAdsProductsUseCase)
    }

    @CategoryNavScope
    @Provides
    fun getAddWishListUseCase(context: Context)
            : AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @CategoryNavScope
    @Provides
    fun getRemoveWishListUseCase(context: Context)
            : RemoveWishListUseCase {
        return RemoveWishListUseCase(context)
    }

    @CategoryNavScope
    @Provides
    fun getSubCategoryV3UseCase(@Named("subCategoryV3GqlUseCaseObject") graphqlUseCase
                                : GraphqlUseCase)
            : SubCategoryV3UseCase {
        return SubCategoryV3UseCase(graphqlUseCase)
    }

    @CategoryNavScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @CategoryNavScope
    @Provides
    fun provideResources(context: Context): Resources {
        return context.resources
    }

    @CategoryNavScope
    @Provides
    fun provideGraphQlRepo(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @CategoryNavScope
    @Provides
    fun provideBaseRepository(): BaseRepository {
        return BaseRepository()
    }

    @CategoryNavScope
    @Provides
    fun provideSendTopAdsUseCase(topAdsUrlHitter: TopAdsUrlHitter) = SendTopAdsUseCase(topAdsUrlHitter)

    @CategoryNavScope
    @Provides
    fun providePageLoadTimePerformanceMonitoring() : PageLoadTimePerformanceInterface {
        return PageLoadTimePerformanceCallback(
                CategoryNavActivity.CATEGORY_LEVELS_PLT_PREPARE_METRICS,
                CategoryNavActivity.CATEGORY_LEVELS_PLT_NETWORK_METRICS,
                CategoryNavActivity.CATEGORY_LEVELS_PLT_RENDER_METRICS,0,0,0,0,null
        )
    }

}