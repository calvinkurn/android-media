package com.tokopedia.hotlist.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common_category.usecase.*
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.hotlist.domain.usecases.HotlistDetailUseCase
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named


@Module
class HotlistNavUseCaseModule {

    @HotlistNavScope
    @Provides
    fun providesContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @HotlistNavScope
    @Named("productGqlUseCaseObject")
    @Provides
    fun provideRxGQLProductUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @HotlistNavScope
    @Provides
    fun provideCategoryProductUseCase(@Named("productGqlUseCaseObject") graphqlUseCase: GraphqlUseCase):
            CategoryProductUseCase {
        return CategoryProductUseCase(graphqlUseCase)
    }

    @HotlistNavScope
    @Provides
    fun getProductListUseCase(categoryProductUseCase: CategoryProductUseCase
                              , topAdsProductsUseCase
                              : TopAdsProductsUseCase)
            : GetProductListUseCase {
        return GetProductListUseCase(categoryProductUseCase, topAdsProductsUseCase)
    }

    @HotlistNavScope
    @Provides
    fun getAddWishListUseCase(context: Context)
            : AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @HotlistNavScope
    @Provides
    fun getRemoveWishListUseCase(context: Context)
            : RemoveWishListUseCase {
        return RemoveWishListUseCase(context)
    }

    @HotlistNavScope
    @Provides
    fun provideSendTopAdsUseCase(topAdsUrlHitter: TopAdsUrlHitter) = SendTopAdsUseCase(topAdsUrlHitter)

}