package com.tokopedia.discovery2.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common.RepositoryProvider
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
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRepository
import com.tokopedia.discovery2.repository.emptystate.EmptyStateRepository
import com.tokopedia.discovery2.repository.horizontalcategory.CategoryNavigationRepository
import com.tokopedia.discovery2.repository.horizontalcategory.CategoryNavigationRestRepository
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository
import com.tokopedia.discovery2.repository.pushstatus.pushstatus.PushStatusGQLRepository
import com.tokopedia.discovery2.repository.pushstatus.pushstatus.PushStatusRepository
import com.tokopedia.discovery2.repository.quickFilter.FilterRepository
import com.tokopedia.discovery2.repository.quickFilter.FilterRestRepository
import com.tokopedia.discovery2.repository.quickFilter.IQuickFilterGqlRepository
import com.tokopedia.discovery2.repository.quickFilter.QuickFilterGQLRepository
import com.tokopedia.discovery2.repository.quickFilter.QuickFilterRepository
import com.tokopedia.discovery2.repository.quickcoupon.QuickCouponGQLRepository
import com.tokopedia.discovery2.repository.quickcoupon.QuickCouponRepository
import com.tokopedia.discovery2.repository.tabs.TabsGQLRepository
import com.tokopedia.discovery2.repository.tabs.TabsRepository
import com.tokopedia.discovery2.repository.tokopoints.TokopointsRepository
import com.tokopedia.discovery2.repository.tokopoints.TokopointsRestRepository
import com.tokopedia.discovery2.usecase.topAdsUseCase.TopAdsTrackingUseCase
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.play.widget.di.PlayWidgetModule
import com.tokopedia.play.widget.domain.PlayWidgetReminderUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUpdateChannelUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.mapper.PlayWidgetMapper
import com.tokopedia.play.widget.ui.type.PlayWidgetSize
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import dagger.Module
import dagger.Provides

@Module(includes = [PlayWidgetModule::class])
class DiscoveryModule(val repoProvider: RepositoryProvider) {

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
    fun provideTabsGQLRepository(): TabsRepository {
        return TabsGQLRepository()
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
        return repoProvider.provideProductCardsRepository()
    }

    @Provides
    fun provideDiscoveryPageRepository(@ApplicationContext context: Context): DiscoveryPageRepository {
        return repoProvider.provideDiscoveryPageRepository(provideGetStringMethod(context))
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
    fun provideQuickCouponGQLRepository(@ApplicationContext context: Context): QuickCouponRepository {
        return QuickCouponGQLRepository(provideGetStringMethod(context))
    }

    @Provides
    fun providesDiscoveryTopAdsTrackingUseCase(topAdsUrlHitter: TopAdsUrlHitter): TopAdsTrackingUseCase {
        return repoProvider.provideTopAdsTrackingUseCase(topAdsUrlHitter)
    }

    @Provides
    fun provideFilterRestRepository(): FilterRepository {
        return repoProvider.provideFilterRepository()
    }

    @Provides
    fun provideQuickFilterRestRepository(): QuickFilterRepository {
        return repoProvider.provideQuickFilterRepository()
    }

    @Provides
    fun provideEmptyStateRepository() : EmptyStateRepository {
        return  repoProvider.provideEmptyStateRepository()
    }

    @Provides
    fun provideQuickFilterGQLRepository(): IQuickFilterGqlRepository {
        return QuickFilterGQLRepository()
    }

    @DiscoveryScope
    @Provides
    fun providePageLoadTimePerformanceMonitoring() : PageLoadTimePerformanceInterface {
        return repoProvider.providePageLoadTimePerformanceMonitoring()
    }

    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    fun providePlayWidget(playWidgetUseCase: PlayWidgetUseCase,
                          playWidgetReminderUseCase: Lazy<PlayWidgetReminderUseCase>,
                          playWidgetUpdateChannelUseCase: Lazy<PlayWidgetUpdateChannelUseCase>,
                          mapperProviders: Map<PlayWidgetSize, @JvmSuppressWildcards PlayWidgetMapper>): PlayWidgetTools {
        return PlayWidgetTools(playWidgetUseCase, playWidgetReminderUseCase, playWidgetUpdateChannelUseCase, mapperProviders)
    }

}