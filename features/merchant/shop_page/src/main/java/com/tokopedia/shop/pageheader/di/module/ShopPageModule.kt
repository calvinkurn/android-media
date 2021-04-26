package com.tokopedia.shop.pageheader.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant
import com.tokopedia.shop.common.constant.GqlQueryConstant
import com.tokopedia.shop.pageheader.ShopPageHeaderConstant
import com.tokopedia.shop.pageheader.di.scope.ShopPageScope
import com.tokopedia.shop.pageheader.domain.interactor.GetBroadcasterShopConfigUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named


@Module(includes = [ShopViewModelModule::class])
class ShopPageModule {

    @ShopPageScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ShopPageScope
    @Provides
    fun provideGetBroadcasterShopConfigUseCase(graphqlUseCase: MultiRequestGraphqlUseCase): GetBroadcasterShopConfigUseCase {
        return GetBroadcasterShopConfigUseCase(graphqlUseCase)
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
            @Named(GQLQueryNamedConstant.SHOP_INFO_FOR_TOP_CONTENT) queryShopInfoTopContent: String,
            @Named(GQLQueryNamedConstant.SHOP_INFO_FOR_CORE_AND_ASSETS) queryShopInfoCoreAssets: String,
            @Named(ShopPageHeaderConstant.SHOP_PAGE_FEED_WHITELIST) queryShopFeedWhitelist: String
    ): Map<String, String> {
        return mapOf(
                GQLQueryNamedConstant.SHOP_INFO_FOR_TOP_CONTENT to queryShopInfoTopContent,
                GQLQueryNamedConstant.SHOP_INFO_FOR_CORE_AND_ASSETS to queryShopInfoCoreAssets,
                ShopPageHeaderConstant.SHOP_PAGE_FEED_WHITELIST to queryShopFeedWhitelist
        )
    }

    @ShopPageScope
    @Provides
    fun provideFirebaseRemoteConfig(@ApplicationContext context: Context) : RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }
}