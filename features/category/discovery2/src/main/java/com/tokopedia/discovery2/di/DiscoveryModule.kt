package com.tokopedia.discovery2.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.repository.campaignsubscribe.CampaignSubscribeGQLRepository
import com.tokopedia.discovery2.repository.campaignsubscribe.CampaignSubscribeRepo
import com.tokopedia.discovery2.repository.claimCoupon.ClaimCouponGQLRepository
import com.tokopedia.discovery2.repository.claimCoupon.ClaimCouponRestRepository
import com.tokopedia.discovery2.repository.claimCoupon.IClaimCouponGqlRepository
import com.tokopedia.discovery2.repository.claimCoupon.IClaimCouponRepository
import com.tokopedia.discovery2.repository.cpmtopads.CpmTopAdsGQLRepository
import com.tokopedia.discovery2.repository.cpmtopads.CpmTopAdsRepository
import com.tokopedia.discovery2.repository.customtopchat.CustomTopChatGqlRepository
import com.tokopedia.discovery2.repository.customtopchat.CustomTopChatRepository
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryDataGQLRepository
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRepository
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryUIConfigGQLRepository
import com.tokopedia.discovery2.repository.horizontalcategory.CategoryNavigationRepository
import com.tokopedia.discovery2.repository.horizontalcategory.CategoryNavigationRestRepository
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository
import com.tokopedia.discovery2.repository.productcards.ProductCardsRestRepository
import com.tokopedia.discovery2.repository.pushstatus.pushstatus.PushStatusGQLRepository
import com.tokopedia.discovery2.repository.pushstatus.pushstatus.PushStatusRepository
import com.tokopedia.discovery2.repository.quickFilter.QuickFilterRepository
import com.tokopedia.discovery2.repository.quickFilter.QuickFilterRestRepository
import com.tokopedia.discovery2.repository.quickcoupon.QuickCouponGQLRepository
import com.tokopedia.discovery2.repository.quickcoupon.QuickCouponRepository
import com.tokopedia.discovery2.repository.tokopoints.TokopointsRepository
import com.tokopedia.discovery2.repository.tokopoints.TokopointsRestRepository
import com.tokopedia.discovery2.usecase.topAdsUseCase.DiscoveryTopAdsTrackingUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DISCOVERY_PLT_NETWORK_METRICS
import com.tokopedia.discovery2.viewcontrollers.activity.DISCOVERY_PLT_PREPARE_METRICS
import com.tokopedia.discovery2.viewcontrollers.activity.DISCOVERY_PLT_RENDER_METRICS
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class DiscoveryModule {

    @Provides
    fun provideBaseRepository(): BaseRepository {
        return BaseRepository()
    }

    @Provides
    fun providePushStatusGQLRepository(@ApplicationContext context: Context): PushStatusRepository {
        return PushStatusGQLRepository(provideGetStringMethod(context))
    }

    @Provides
    fun provideCpmTopAdsGQLRepository(): CpmTopAdsRepository {
        return CpmTopAdsGQLRepository()
    }

    @Provides
    fun provideCustomTopChatRepository(@ApplicationContext context: Context): CustomTopChatRepository {
        return CustomTopChatGqlRepository(provideGetStringMethod(context))
    }

    @Provides
    fun provideCategoryNavigationRestRepository(): CategoryNavigationRepository {
        return CategoryNavigationRestRepository()
    }

    @Provides
    fun provideRedeemCouponRepository(@ApplicationContext context: Context): IClaimCouponGqlRepository {
        return ClaimCouponGQLRepository(provideGetStringMethod(context))
    }


    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideClaimCouponRestRepository(): IClaimCouponRepository {
        return ClaimCouponRestRepository()
    }

    @Provides
    fun provideTokopointsRestRepository(): TokopointsRepository {
        return TokopointsRestRepository()
    }

    @Provides
    fun provideProductCardsRestRepository(): ProductCardsRepository {
        return ProductCardsRestRepository()
    }

    @Provides
    fun provideDiscoveryPageRepository(@ApplicationContext context: Context): DiscoveryPageRepository {
        return DiscoveryDataGQLRepository(provideGetStringMethod(context))
    }

    @Provides
    fun provideGetStringMethod(@ApplicationContext context: Context): (Int) -> String {
        return { id -> GraphqlHelper.loadRawString(context.resources, id) }
    }

    @Provides
    fun providesTrackingQueue(@ApplicationContext context: Context): TrackingQueue = TrackingQueue(context)

    @Provides
    fun provideCampaignSubscribeGQLRepository(@ApplicationContext context: Context): CampaignSubscribeRepo {
        return CampaignSubscribeGQLRepository(provideGetStringMethod(context))
    }

    @Provides
    fun provideDiscoveryUIConfigRepository(@ApplicationContext context: Context): DiscoveryUIConfigGQLRepository {
        return DiscoveryUIConfigGQLRepository(provideDiscoveryUIConfigQuery(context))
    }

    @Provides
    fun provideDiscoveryUIConfigQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_discovery_ui_config)
    }

    @Provides
    fun provideQuickCouponGQLRepository(@ApplicationContext context: Context): QuickCouponRepository {
        return QuickCouponGQLRepository(provideGetStringMethod(context))
    }

    @Provides
    fun providesDiscoveryTopAdsTrackingUseCase(topAdsUrlHitter: TopAdsUrlHitter): DiscoveryTopAdsTrackingUseCase {
        return DiscoveryTopAdsTrackingUseCase(topAdsUrlHitter)
    }

    @Provides
    fun provideQuickFilterRestRepository(): QuickFilterRepository {
        return QuickFilterRestRepository()
    }

    @DiscoveryScope
    @Provides
    fun providePageLoadTimePerformanceMonitoring() : PageLoadTimePerformanceInterface {
        return PageLoadTimePerformanceCallback(
                DISCOVERY_PLT_PREPARE_METRICS,
                DISCOVERY_PLT_NETWORK_METRICS,
                DISCOVERY_PLT_RENDER_METRICS,0,0,0,0,null
        )
    }
}