package com.tokopedia.shop.pageheader.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant
import com.tokopedia.shop.pageheader.ShopPageHeaderConstant
import com.tokopedia.shop.pageheader.di.scope.ShopPageHeaderScope
import com.tokopedia.shop.pageheader.domain.interactor.GetBroadcasterAuthorConfig
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.universal_sharing.view.usecase.AffiliateEligibilityCheckUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [ShopPageHeaderViewModelModule::class])
class ShopPageHeaderModule {

    @ShopPageHeaderScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ShopPageHeaderScope
    @Provides
    fun provideGetBroadcasterAuthorConfigUseCase(graphqlUseCase: MultiRequestGraphqlUseCase): GetBroadcasterAuthorConfig {
        return GetBroadcasterAuthorConfig(graphqlUseCase)
    }

    @ShopPageHeaderScope
    @Provides
    @Named(ShopPageHeaderConstant.SHOP_PAGE_FEED_WHITELIST)
    fun provideGqlQueryShopFeedWhitelist(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(
            context.resources,
            com.tokopedia.feedcomponent.R.raw.query_whitelist
        )
    }

    @ShopPageHeaderScope
    @Provides
    @Named(GQLQueryNamedConstant.SHOP_PAGE_P1_QUERIES)
    fun provideShopPageP1Queries(
        @Named(GQLQueryNamedConstant.SHOP_INFO_FOR_CORE_AND_ASSETS) queryShopInfoCoreAssets: String,
        @Named(ShopPageHeaderConstant.SHOP_PAGE_FEED_WHITELIST) queryShopFeedWhitelist: String
    ): Map<String, String> {
        return mapOf(
            GQLQueryNamedConstant.SHOP_INFO_FOR_CORE_AND_ASSETS to queryShopInfoCoreAssets,
            ShopPageHeaderConstant.SHOP_PAGE_FEED_WHITELIST to queryShopFeedWhitelist
        )
    }

    @ShopPageHeaderScope
    @Provides
    fun provideFirebaseRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @ShopPageHeaderScope
    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context) = TrackingQueue(context)

    @ShopPageHeaderScope
    @Provides
    fun provideAffiliateUseCase(graphqlRepository: GraphqlRepository): AffiliateEligibilityCheckUseCase {
        return AffiliateEligibilityCheckUseCase(graphqlRepository)
    }
}
