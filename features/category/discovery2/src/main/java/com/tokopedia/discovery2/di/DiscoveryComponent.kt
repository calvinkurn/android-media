package com.tokopedia.discovery.categoryrevamp.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.discovery2.di.DiscoveryModule
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners.MultiBannerViewModel
import dagger.Component


@DiscoveryScope
@Component(modules = [DiscoveryModule::class,DiscoveryViewModelModule::class], dependencies = [BaseAppComponent::class])
interface DiscoveryComponent {
    fun inject(discoveryActivity: DiscoveryActivity)
    fun inject(mutliViewModel: MultiBannerViewModel)
}