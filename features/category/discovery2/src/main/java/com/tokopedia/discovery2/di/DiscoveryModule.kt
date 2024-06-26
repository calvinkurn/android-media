package com.tokopedia.discovery2.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common.RepositoryProvider
import com.tokopedia.discovery2.DiscoveryAppLogPageState
import com.tokopedia.discovery2.repository.automatecoupon.AutomateCouponGqlRepository
import com.tokopedia.discovery2.repository.automatecoupon.IAutomateCouponGqlRepository
import com.tokopedia.discovery2.repository.banner.BannerGQLRepository
import com.tokopedia.discovery2.repository.banner.BannerRepository
import com.tokopedia.discovery2.repository.bannerinfinite.BannerInfiniteGQLRepository
import com.tokopedia.discovery2.repository.bannerinfinite.BannerInfiniteRepository
import com.tokopedia.discovery2.repository.campaignsubscribe.CampaignSubscribeGQLRepository
import com.tokopedia.discovery2.repository.campaignsubscribe.CampaignSubscribeRepo
import com.tokopedia.discovery2.repository.claimCoupon.ClaimCouponGQLRepository
import com.tokopedia.discovery2.repository.claimCoupon.ClaimCouponRestRepository
import com.tokopedia.discovery2.repository.claimCoupon.IClaimCouponGqlRepository
import com.tokopedia.discovery2.repository.claimCoupon.IClaimCouponRepository
import com.tokopedia.discovery2.repository.contentCard.ContentCardGQLRepository
import com.tokopedia.discovery2.repository.contentCard.ContentCardRepository
import com.tokopedia.discovery2.repository.customtopchat.CustomTopChatGqlRepository
import com.tokopedia.discovery2.repository.customtopchat.CustomTopChatRepository
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRepository
import com.tokopedia.discovery2.repository.emptystate.EmptyStateRepository
import com.tokopedia.discovery2.repository.flashsaletoko.FlashSaleTokoGQLRepository
import com.tokopedia.discovery2.repository.flashsaletoko.FlashSaleTokoRepository
import com.tokopedia.discovery2.repository.merchantvoucher.MerchantVoucherGQLRepository
import com.tokopedia.discovery2.repository.merchantvoucher.MerchantVoucherRepository
import com.tokopedia.discovery2.repository.mycoupon.MyCouponGQLRepository
import com.tokopedia.discovery2.repository.mycoupon.MyCouponRepository
import com.tokopedia.discovery2.repository.productbundling.ProductBundlingGQLRepository
import com.tokopedia.discovery2.repository.productbundling.ProductBundlingRepository
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository
import com.tokopedia.discovery2.repository.producthighlightrepository.ProductHighlightGQLRepository
import com.tokopedia.discovery2.repository.producthighlightrepository.ProductHighlightRepository
import com.tokopedia.discovery2.repository.pushstatus.pushstatus.PushStatusGQLRepository
import com.tokopedia.discovery2.repository.pushstatus.pushstatus.PushStatusRepository
import com.tokopedia.discovery2.repository.quickFilter.FilterRepository
import com.tokopedia.discovery2.repository.quickFilter.IQuickFilterGqlRepository
import com.tokopedia.discovery2.repository.quickFilter.QuickFilterGQLRepository
import com.tokopedia.discovery2.repository.quickFilter.QuickFilterRepository
import com.tokopedia.discovery2.repository.quickcoupon.QuickCouponGQLRepository
import com.tokopedia.discovery2.repository.quickcoupon.QuickCouponRepository
import com.tokopedia.discovery2.repository.section.SectionGQLRepository
import com.tokopedia.discovery2.repository.section.SectionRepository
import com.tokopedia.discovery2.repository.shopcard.ShopCardGQLRepository
import com.tokopedia.discovery2.repository.shopcard.ShopCardRepository
import com.tokopedia.discovery2.repository.supportingbrand.SupportingBrandGQLRepository
import com.tokopedia.discovery2.repository.supportingbrand.SupportingBrandRepository
import com.tokopedia.discovery2.repository.tabs.TabsGQLRepository
import com.tokopedia.discovery2.repository.tabs.TabsRepository
import com.tokopedia.discovery2.repository.topads.TopAdsHeadlineRepository
import com.tokopedia.discovery2.usecase.topAdsUseCase.TopAdsTrackingUseCase
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.play.widget.di.PlayWidgetModule
import com.tokopedia.play.widget.domain.PlayWidgetReminderUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUpdateChannelUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.mapper.PlayWidgetUiMapper
import com.tokopedia.play.widget.util.PlayWidgetConnectionUtil
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import dagger.Module
import dagger.Provides
import javax.inject.Named
import com.tokopedia.atc_common.R as atc_commonR

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
    fun provideTabsGQLRepository(): TabsRepository {
        return TabsGQLRepository()
    }

    @Provides
    fun provideFlashSaleTokoGQLRepository(): FlashSaleTokoRepository {
        return FlashSaleTokoGQLRepository()
    }

    @Provides
    fun provideCustomTopChatRepository(@ApplicationContext context: Context): CustomTopChatRepository {
        return CustomTopChatGqlRepository(provideGetStringMethod(context))
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
    fun provideProductCardsRestRepository(): ProductCardsRepository {
        return repoProvider.provideProductCardsRepository()
    }

    @Provides
    fun provideMerchantVoucherRepository(): MerchantVoucherRepository {
        return MerchantVoucherGQLRepository()
    }

    @Provides
    fun provideBannerRepository(): BannerRepository {
        return BannerGQLRepository()
    }

    @Provides
    fun provideTopAdsHeadlineRepository(): TopAdsHeadlineRepository {
        return repoProvider.provideTopAdsHeadlineRepository()
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
    fun provideEmptyStateRepository(): EmptyStateRepository {
        return repoProvider.provideEmptyStateRepository()
    }

    @Provides
    fun provideQuickFilterGQLRepository(): IQuickFilterGqlRepository {
        return QuickFilterGQLRepository()
    }

    @Provides
    fun provideShopCardRepository(): ShopCardRepository {
        return ShopCardGQLRepository()
    }

    @Provides
    fun provideProductHighlightRepository(): ProductHighlightRepository {
        return ProductHighlightGQLRepository()
    }

    @Provides
    fun provideProductBundlingRepository(): ProductBundlingRepository {
        return ProductBundlingGQLRepository()
    }

    @Provides
    fun provideSectionRepository(): SectionRepository {
        return SectionGQLRepository()
    }

    @Provides
    fun provideMyCouponRepository(@ApplicationContext context: Context): MyCouponRepository {
        return MyCouponGQLRepository(provideGetStringMethod(context))
    }

    @Provides
    fun provideBannerInfiniteRepository(): BannerInfiniteRepository {
        return BannerInfiniteGQLRepository()
    }

    @DiscoveryScope
    @Provides
    fun providePageLoadTimePerformanceMonitoring(): PageLoadTimePerformanceInterface {
        return repoProvider.providePageLoadTimePerformanceMonitoring()
    }

    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    fun providePlayWidget(
        playWidgetUseCase: PlayWidgetUseCase,
        playWidgetReminderUseCase: Lazy<PlayWidgetReminderUseCase>,
        playWidgetUpdateChannelUseCase: Lazy<PlayWidgetUpdateChannelUseCase>,
        mapper: PlayWidgetUiMapper,
        connectionUtil: PlayWidgetConnectionUtil
    ): PlayWidgetTools {
        return PlayWidgetTools(
            playWidgetUseCase,
            playWidgetReminderUseCase,
            playWidgetUpdateChannelUseCase,
            mapper,
            connectionUtil
        )
    }

    @Provides
    fun provideContentCardGQLRepository(): ContentCardRepository {
        return ContentCardGQLRepository()
    }

    @Provides
    @Named("atcOcsMutation")
    fun provideAddToCartOcsMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, atc_commonR.raw.mutation_add_to_cart_one_click_shipment)
    }

    @Provides
    fun provideRemoteConfig(
        @ApplicationContext context: Context
    ): RemoteConfig = FirebaseRemoteConfigImpl(context)

    @Provides
    fun provideSupportingBrandRepository(): SupportingBrandRepository {
        return SupportingBrandGQLRepository()
    }

    @Provides
    fun provideAutomateCouponRepository(@ApplicationContext context: Context): IAutomateCouponGqlRepository {
        return AutomateCouponGqlRepository(provideGetStringMethod(context))
    }

    @Provides
    @DiscoveryScope
    fun provideAppLogPageState(): DiscoveryAppLogPageState {
        return DiscoveryAppLogPageState()
    }
}
