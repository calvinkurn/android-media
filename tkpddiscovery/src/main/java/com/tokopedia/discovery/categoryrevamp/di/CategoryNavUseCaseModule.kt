package com.tokopedia.discovery.categoryrevamp.di

import android.content.Context
import android.content.res.Resources
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.discovery.categoryrevamp.domain.usecase.*
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
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

    @CategoryNavScope
    @Provides
    fun provideDynamicFilterUseCase(context: Context): DynamicFilterUseCase {
        return DynamicFilterUseCase(context)
    }

    @CategoryNavScope
    @Provides
    fun provideQuickFilterUseCase(context: Context): QuickFilterUseCase {
        return QuickFilterUseCase(context)
    }

    @CategoryNavScope
    @Named("topAdsProductListing")
    @Provides
    fun provideTopAdsUseCase(context: Context): TopAdsProductsUseCase {
        return TopAdsProductsUseCase(context)
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
    fun getSubCategoryV3UseCase(context: Context, @Named("subCategoryV3GqlUseCaseObject") graphqlUseCase
    : GraphqlUseCase)
            : SubCategoryV3UseCase {
        return SubCategoryV3UseCase(context, graphqlUseCase)
    }

    @CategoryNavScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @CategoryNavScope
    @Provides
    fun provideResources(context: Context): Resources{
        return context.resources
    }

    @CategoryNavScope
    @Provides
    fun provideGraphQlRepo(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

}