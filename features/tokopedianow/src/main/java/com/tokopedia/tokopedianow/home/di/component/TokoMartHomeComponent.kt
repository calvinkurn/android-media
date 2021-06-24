package com.tokopedia.tokopedianow.home.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.home.di.module.TokoMartHomeModule
import com.tokopedia.tokopedianow.home.di.module.TokoMartHomeUseCaseModule
import com.tokopedia.tokopedianow.home.di.module.TokoMartHomeViewModelModule
import com.tokopedia.tokopedianow.home.di.scope.TokoMartHomeScope
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoMartHomeFragment
import dagger.Component

@TokoMartHomeScope
@Component(
    modules = [
        TokoMartHomeModule::class,
        TokoMartHomeUseCaseModule::class,
        TokoMartHomeViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface TokoMartHomeComponent {

    fun inject(fragment: TokoMartHomeFragment)
}