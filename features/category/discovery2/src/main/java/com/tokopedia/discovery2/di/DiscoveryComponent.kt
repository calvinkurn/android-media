package com.tokopedia.discovery2.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners.MultiBannerViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorynavigation.CategoryNavigationViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tokopoints.TokopointsViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon.ClaimCouponItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon.ClaimCouponViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.cpmtopads.CpmTopAdsViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.ProductCardCarouselViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardrevamp.ProductCardRevampViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardsprintsalecarousel.ProductCardSprintSaleCarouselViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs.TabsItemViewModel
import dagger.Component


@DiscoveryScope
@Component(modules = [DiscoveryModule::class,DiscoveryViewModelModule::class], dependencies = [BaseAppComponent::class])
interface DiscoveryComponent {
    fun inject(discoveryActivity: DiscoveryActivity)
    fun inject(mutliViewModel: MultiBannerViewModel)
    fun inject(tokopointsViewModel: TokopointsViewModel)
    fun inject(productCardCarouselViewModel: ProductCardCarouselViewModel)
    fun inject(productCardSprintSaleCarouselViewModel: ProductCardSprintSaleCarouselViewModel )
    fun inject(productCardRevampViewModel: ProductCardRevampViewModel)
    fun inject(cpmTopAdsViewModel: CpmTopAdsViewModel)
    fun inject(categoryNavigationViewModel: CategoryNavigationViewModel)
    fun inject(claimCouponViewModel: ClaimCouponViewModel)
    fun inject(claimCouponItemViewModel: ClaimCouponItemViewModel)
}