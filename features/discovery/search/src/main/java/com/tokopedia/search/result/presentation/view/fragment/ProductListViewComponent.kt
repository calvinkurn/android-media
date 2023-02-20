package com.tokopedia.search.result.presentation.view.fragment

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.recommendation_widget_common.di.RecommendationModule
import com.tokopedia.search.di.module.FilterControllerModule
import com.tokopedia.search.di.module.IrisModule
import com.tokopedia.search.di.module.RecycledViewPoolModule
import com.tokopedia.search.di.module.RemoteConfigModule
import com.tokopedia.search.di.module.SearchContextModule
import com.tokopedia.search.di.module.SearchNavigationListenerModule
import com.tokopedia.search.di.module.SearchOnBoardingLocalCacheModule
import com.tokopedia.search.di.module.StaggeredGridLayoutManagerModule
import com.tokopedia.search.di.module.TrackingQueueModule
import com.tokopedia.search.di.module.UserSessionModule
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.domain.usecase.getdynamicfilter.GetDynamicFilterGqlUseCaseModule
import com.tokopedia.search.result.domain.usecase.getinspirationcarouselchips.GetInspirationCarouselChipsProductUseCaseModule
import com.tokopedia.search.result.domain.usecase.getlocalsearchrecommendation.GetLocalSearchRecommendationUseCaseModule
import com.tokopedia.search.result.domain.usecase.getproductcount.GetProductCountUseCaseModule
import com.tokopedia.search.result.domain.usecase.savelastfilter.SaveLastFilterUseCaseModule
import com.tokopedia.search.result.domain.usecase.searchproduct.SearchProductUseCaseModule
import com.tokopedia.search.result.domain.usecase.searchsamesessionrecommendation.SearchSameSessionRecommendationUseCaseModule
import com.tokopedia.search.result.presentation.presenter.product.ProductListPresenterModule
import com.tokopedia.search.result.product.banned.BannedProductsViewModule
import com.tokopedia.search.result.product.broadmatch.BroadMatchModule
import com.tokopedia.search.result.product.chooseaddress.ChooseAddressViewModule
import com.tokopedia.search.result.product.cpm.TopAdsHeadlineModule
import com.tokopedia.search.result.product.filter.bottomsheetfilter.BottomSheetFilterModule
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselModule
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcModule
import com.tokopedia.search.result.product.lastfilter.LastFilterModule
import com.tokopedia.search.result.product.pagination.PaginationModule
import com.tokopedia.search.result.product.performancemonitoring.PerformanceMonitoringModule
import com.tokopedia.search.result.product.safesearch.SafeSearchModule
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationModule
import com.tokopedia.search.result.product.ticker.TickerModule
import com.tokopedia.search.result.product.video.VideoModule
import com.tokopedia.search.result.product.wishlist.WishlistModule
import com.tokopedia.search.utils.ProductionSchedulersProviderModule
import com.tokopedia.search.utils.applinkmodifier.SearchApplinkModifierModule
import com.tokopedia.topads.sdk.di.TopAdsUrlHitterModule
import dagger.Component

@SearchScope
@Component(modules = [
    SearchContextModule::class,
    RemoteConfigModule::class,
    RecommendationModule::class,
    UserSessionModule::class,
    SearchProductUseCaseModule::class,
    GetProductCountUseCaseModule::class,
    GetDynamicFilterGqlUseCaseModule::class,
    GetLocalSearchRecommendationUseCaseModule::class,
    SearchOnBoardingLocalCacheModule::class,
    TopAdsUrlHitterModule::class,
    TopAdsHeadlineModule::class,
    ProductionSchedulersProviderModule::class,
    GetInspirationCarouselChipsProductUseCaseModule::class,
    SaveLastFilterUseCaseModule::class,
    SearchSameSessionRecommendationUseCaseModule::class,
    SameSessionRecommendationModule::class,
    IrisModule::class,
    PerformanceMonitoringModule::class,
    ChooseAddressViewModule::class,
    PaginationModule::class,
    TrackingQueueModule::class,
    ProductListProvidersModule::class,
    FilterControllerModule::class,
    ProductListPresenterModule::class,
    StaggeredGridLayoutManagerModule::class,
    BannedProductsViewModule::class,
    RecycledViewPoolModule::class,
    SearchNavigationListenerModule::class,
    InspirationListAtcModule::class,
    BroadMatchModule::class,
    SearchApplinkModifierModule::class,
    InspirationCarouselModule::class,
    VideoModule::class,
    TickerModule::class,
    SafeSearchModule::class,
    WishlistModule::class,
    LastFilterModule::class,
    BottomSheetFilterModule::class,
 ], dependencies = [BaseAppComponent::class])
interface ProductListViewComponent {

    fun inject(view: ProductListFragment)
}
