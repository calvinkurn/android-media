package com.tokopedia.play.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

/**
 * Created by kenny.hadisaputra on 21/03/22
 */
@PlayScope
@Component(
    modules = [
        PlayTestModule::class,
        PlayViewModelModule::class,
        PlayViewerFragmentModule::class,
        PlayBindModule::class,
        PlayTestRepositoryModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
interface PlayTestComponent : PlayComponent {
}