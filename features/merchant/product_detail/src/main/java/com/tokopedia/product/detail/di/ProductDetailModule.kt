package com.tokopedia.product.detail.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.product.detail.R
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import javax.inject.Named

@ProductDetailScope
@Module
class ProductDetailModule {

    @Provides
    @ProductDetailScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = com.tokopedia.user.session.UserSession(context)

    @ProductDetailScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @ProductDetailScope
    @Provides
    @Named("Main")
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_SHOP)
    fun provideRawShopQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_shop_info)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_WISHLIST_STATUS)
    fun provideRawWishlistQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_is_wishlisted)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_PRODUCT_RATING)
    fun provideRawProductRatingQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_product_rating)
}