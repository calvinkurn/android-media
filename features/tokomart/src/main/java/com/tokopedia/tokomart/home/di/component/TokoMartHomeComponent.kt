package com.tokopedia.tokomart.home.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokomart.home.di.module.TokoMartHomeModule
import com.tokopedia.tokomart.home.di.module.TokoMartHomeUseCaseModule
import com.tokopedia.tokomart.home.di.module.TokoMartHomeViewModelModule
import com.tokopedia.tokomart.home.di.scope.TokoMartHomeScope
import com.tokopedia.tokomart.home.presentation.fragment.TokoMartHomeFragment
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