package com.tokopedia.tokopedianow.home.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.home.di.module.HomeModule
import com.tokopedia.tokopedianow.home.di.module.HomeUseCaseModule
import com.tokopedia.tokopedianow.home.di.module.HomeViewModelModule
import com.tokopedia.tokopedianow.home.di.scope.HomeScope
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment
import dagger.Component

@HomeScope
@Component(
    modules = [
        HomeModule::class,
        HomeUseCaseModule::class,
        HomeViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface HomeComponent {

    fun inject(fragmentTokoNow: TokoNowHomeFragment)
}