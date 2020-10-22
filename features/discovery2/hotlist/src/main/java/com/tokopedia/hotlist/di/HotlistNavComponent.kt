package com.tokopedia.hotlist.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.hotlist.fragment.HotlistNavFragment
import com.tokopedia.topads.sdk.di.TopAdsUrlHitterModule
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import dagger.Component

@HotlistNavScope
@Component(modules = [HotlistNavUseCaseModule::class,
    HotListNavVewModelModule::class,
    TopAdsWishlistModule::class,
    TopAdsUrlHitterModule::class],
        dependencies = [BaseAppComponent::class])
interface HotlistNavComponent {
    fun inject(hotlistNavFragment: HotlistNavFragment)
}