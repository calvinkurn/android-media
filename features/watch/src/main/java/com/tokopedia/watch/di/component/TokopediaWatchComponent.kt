package com.tokopedia.watch.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.watch.TokopediaWatchActivity
import com.tokopedia.watch.di.module.TokopediaWatchModule
import com.tokopedia.watch.di.module.TokopediaWatchViewModelModule
import com.tokopedia.watch.di.scope.TokopediaWatchScope
import dagger.Component

@TokopediaWatchScope
@Component(
    modules = [
        TokopediaWatchModule::class,
        TokopediaWatchViewModelModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
interface TokopediaWatchComponent {
    fun inject(fragmentTokoNow: TokopediaWatchActivity)
}