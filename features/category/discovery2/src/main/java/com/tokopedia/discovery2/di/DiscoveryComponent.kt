package com.tokopedia.discovery2.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.common_sdk_affiliate_toko.di.AffiliateCommonSdkModule
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners.MultiBannerViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon.ClaimCouponItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon.ClaimCouponViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.explicitwidget.ExplicitWidgetViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.loadmore.LoadMoreViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem.MasterProductCardItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon.MyCouponItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon.MyCouponViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.ProductCardCarouselViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate.EmptyStateViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardrevamp.ProductCardRevampViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickcoupon.QuickCouponViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickfilter.QuickFilterViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs.TabsViewModel
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryExtensibleFragment
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.topads.sdk.di.TopAdsUrlHitterModule
import com.tokopedia.usercomponents.explicit.di.ExplicitViewModule
import dagger.Component


@DiscoveryScope
@Component(modules = [DiscoveryModule::class,
    DiscoveryViewModelModule::class,
    TopAdsUrlHitterModule::class,
    AffiliateCommonSdkModule::class,
    ExplicitViewModule::class], dependencies = [BaseAppComponent::class])
interface DiscoveryComponent {
    fun inject(discoveryActivity: DiscoveryActivity)
    fun inject(discoveryExtensibleFragment: DiscoveryExtensibleFragment)
    fun inject(discoveryFragment: DiscoveryFragment)
    fun inject(mutliViewModel: MultiBannerViewModel)
    fun inject(productCardCarouselViewModel: ProductCardCarouselViewModel)
    fun inject(productCardRevampViewModel: ProductCardRevampViewModel)
    fun inject(tabsViewModel: TabsViewModel)
    fun inject(claimCouponViewModel: ClaimCouponViewModel)
    fun inject(claimCouponItemViewModel: ClaimCouponItemViewModel)
    fun inject(masterProductCardItemViewModel: MasterProductCardItemViewModel)
    fun inject(loadMoreViewModel: LoadMoreViewModel)
    fun inject(quickCouponViewModel: QuickCouponViewModel)
    fun inject(quickFilterViewModel: QuickFilterViewModel)
    fun inject(emptyStateViewModel: EmptyStateViewModel)
    fun inject(myCouponViewModel: MyCouponViewModel)
    fun inject(myCouponItemViewModel: MyCouponItemViewModel)
    fun inject(explicitWidgetViewModel: ExplicitWidgetViewModel)
    fun provideSubComponent() : UIWidgetComponent
}
