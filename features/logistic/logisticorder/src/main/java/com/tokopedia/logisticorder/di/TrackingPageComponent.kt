package com.tokopedia.logisticorder.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticorder.view.TrackingPageFragment
import dagger.Component

@ActivityScope
@Component(modules = [TrackingPageModule::class, TrackingPageViewModelModule::class], dependencies = [BaseAppComponent::class])
interface TrackingPageComponent {
    fun inject(trackingPageFragment: TrackingPageFragment)
}