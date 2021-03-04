package com.tokopedia.shop.pageheader.di.module;

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant
import com.tokopedia.shop.common.constant.GqlQueryConstant
import com.tokopedia.shop.common.domain.interactor.DeleteShopInfoCacheUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.pageheader.ShopPageHeaderConstant
import com.tokopedia.shop.pageheader.di.scope.ShopPageScope
import com.tokopedia.shop.pageheader.domain.interactor.GetBroadcasterShopConfigUseCase
import com.tokopedia.shop.product.data.GQLQueryConstant
import com.tokopedia.shop.product.domain.interactor.DeleteShopProductAceUseCase
import com.tokopedia.shop.product.domain.interactor.DeleteShopProductTomeUseCase
import com.tokopedia.shop.product.domain.interactor.DeleteShopProductUseCase
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named


@Module(includes = [ShopViewModelModule::class])
class ShopPageModule {

    @ShopPageScope
    @Provides
    fun provideDeleteShopInfoUseCase(@ApplicationContext context: Context): DeleteShopInfoCacheUseCase {
        return DeleteShopInfoCacheUseCase(context);
    }

    @ShopPageScope
    @Provides
    fun provideDeleteShopProductAceUseCase(@ApplicationContext context: Context): DeleteShopProductAceUseCase {
        return DeleteShopProductAceUseCase(context);
    }

    @ShopPageScope
    @Provides
    fun provideDeleteShopProductTomeUseCase(@ApplicationContext context: Context): DeleteShopProductTomeUseCase {
        return DeleteShopProductTomeUseCase(context);
    }

    @ShopPageScope
    @Provides
    fun provideDeleteShopProductUseCase(@ApplicationContext context: Context): DeleteShopProductUseCase {
        return DeleteShopProductUseCase(provideDeleteShopProductAceUseCase(context), provideDeleteShopProductTomeUseCase(context));
    }

    @ShopPageScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context);
    }

    @ShopPageScope
    @Provides
    fun provideGetBroadcasterShopConfigUseCase(graphqlUseCase: MultiRequestGraphqlUseCase): GetBroadcasterShopConfigUseCase {
        return GetBroadcasterShopConfigUseCase(graphqlUseCase)
    }

    @ShopPageScope
    @Provides
    @Named(GQLQueryConstant.SHOP_PRODUCT)
    fun getShopProductQuery(@ApplicationContext context: Context): String {
        return """
            query getShopProduct(${'$'}shopId: String!,${'$'}filter: ProductListFilter!){
              GetShopProduct(shopID:${'$'}shopId, filter:${'$'}filter){
                status
                errors
                data {
                  product_id
                  name
                  product_url
                  stock
                  status
                  price{
                    text_idr
                  }
                  flags{
                    isFeatured
                    isPreorder
                    isFreereturn
                    isVariant
                    isWholesale
                    isWishlist
                    isSold
                    supportFreereturn
                    mustInsurance
                    withStock
                  }
                  stats{
                    reviewCount
                    rating
                  }
                  campaign{
                    original_price
                    original_price_fmt
                    discounted_price_fmt
                    discounted_percentage
                    discounted_price
                  }
                  primary_image{
                    original
                    thumbnail
                    resize300
                  }
                  cashback{
                    cashback
                    cashback_amount
                  }
                  freeOngkir {
                    isActive
                    imgURL
                  }
                  label_groups {
                    position
                    type
                    title
                    url
                  }
                }
                totalData
              }
            }
        """.trimIndent()
    }

    @ShopPageScope
    @Provides
    @Named(ShopPageHeaderConstant.SHOP_PAGE_GET_HOME_TYPE)
    fun getShopPageHomeTypeQuery(@ApplicationContext context: Context): String {
        return """
            query shopPageGetHomeType(${'$'}shopID: Int!){
              shopPageGetHomeType(
                shopID: ${'$'}shopID
              ){
                shopHomeType 
              }
            }
        """.trimIndent()
    }

    @ShopPageScope
    @Provides
    fun getShopProductUseCase(@Named(GQLQueryConstant.SHOP_PRODUCT) gqlQuery: String?,
                              gqlUseCase: MultiRequestGraphqlUseCase?): GqlGetShopProductUseCase {
        return GqlGetShopProductUseCase(gqlQuery!!, gqlUseCase!!)
    }

    @ShopPageScope
    @Provides
    @Named(GQLQueryNamedConstant.GET_IS_OFFICIAL)
    fun provideGqlQueryGetIsOfficial(): String {
        return GqlQueryConstant.QUERY_GET_IS_OFFICIAL
    }

    @ShopPageScope
    @Provides
    @Named(GQLQueryNamedConstant.GET_IS_POWER_MERCHANT)
    fun provideGqlQueryGetIsPowerMerchant(): String {
        return GqlQueryConstant.QUERY_GET_IS_POWER_MERCHANT
    }

    @ShopPageScope
    @Provides
    @Named(ShopPageHeaderConstant.SHOP_PAGE_FEED_WHITELIST)
    fun provideGqlQueryShopFeedWhitelist(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(
                context.resources,
                com.tokopedia.feedcomponent.R.raw.query_whitelist
        )
    }

    @ShopPageScope
    @Provides
    @Named(GQLQueryNamedConstant.SHOP_PAGE_P1_QUERIES)
    fun provideShopPageP1Queries(
            @Named(GQLQueryNamedConstant.GET_IS_OFFICIAL) queryGetIsOfficial: String,
            @Named(GQLQueryNamedConstant.GET_IS_POWER_MERCHANT) queryGetIsPowerMerchant: String,
            @Named(GQLQueryNamedConstant.SHOP_INFO_FOR_TOP_CONTENT) queryShopInfoTopContent: String,
            @Named(ShopPageHeaderConstant.SHOP_PAGE_GET_HOME_TYPE) queryShopHomeType: String,
            @Named(GQLQueryNamedConstant.SHOP_INFO_FOR_CORE_AND_ASSETS) queryShopInfoCoreAssets: String,
            @Named(ShopPageHeaderConstant.SHOP_PAGE_FEED_WHITELIST) queryShopFeedWhitelist: String
    ): Map<String, String> {
        return mapOf(
                GQLQueryNamedConstant.GET_IS_OFFICIAL to queryGetIsOfficial,
                GQLQueryNamedConstant.GET_IS_POWER_MERCHANT to queryGetIsPowerMerchant,
                GQLQueryNamedConstant.SHOP_INFO_FOR_TOP_CONTENT to queryShopInfoTopContent,
                ShopPageHeaderConstant.SHOP_PAGE_GET_HOME_TYPE to queryShopHomeType,
                GQLQueryNamedConstant.SHOP_INFO_FOR_CORE_AND_ASSETS to queryShopInfoCoreAssets,
                ShopPageHeaderConstant.SHOP_PAGE_FEED_WHITELIST to queryShopFeedWhitelist
        )
    }

    @ShopPageScope
    @Provides
    fun getCoroutineDispatcherProvider(): CoroutineDispatchers {
        return CoroutineDispatchersProvider
    }

    @ShopPageScope
    @Provides
    fun provideFirebaseRemoteConfig(@ApplicationContext context: Context) : RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }
}