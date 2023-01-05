package com.tokopedia.watch.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component
import com.tokopedia.watch.listenerservice.DataLayerServiceListener

@TkpdWatchScope
@Component(modules = [TkpdWatchUseCaseModule::class], dependencies = [BaseAppComponent::class])
interface TkpdWatchComponent {
    fun inject(dataLayerServiceListener: DataLayerServiceListener)
}