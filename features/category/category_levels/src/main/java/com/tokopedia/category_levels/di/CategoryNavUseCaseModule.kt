package com.tokopedia.category_levels.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common_category.usecase.*
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
            : com.tokopedia.category_levels.domain.usecase.SubCategoryV3UseCase {
        return com.tokopedia.category_levels.domain.usecase.SubCategoryV3UseCase(context, graphqlUseCase)
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
    fun provideSendTopAdsUseCase() = SendTopAdsUseCase()

}