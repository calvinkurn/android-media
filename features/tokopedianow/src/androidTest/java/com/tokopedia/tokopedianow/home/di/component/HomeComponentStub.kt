package com.tokopedia.tokopedianow.home.di.component

import com.tokopedia.tokopedianow.test.di.component.BaseAppComponentStub
import com.tokopedia.tokopedianow.home.di.module.HomeModuleStub
import com.tokopedia.tokopedianow.home.di.module.HomeViewModelModule
import com.tokopedia.tokopedianow.home.di.scope.HomeScope
import com.tokopedia.tokopedianow.home.presentation.activity.TokoNowHomeActivityTestFixture
import dagger.Component

@HomeScope
@Component(
    modules = [
        HomeModuleStub::class,
        HomeViewModelModule::class,
    ],
    dependencies = [BaseAppComponentStub::class]
)
interface HomeComponentStub : HomeComponent {
    fun inject(activity: TokoNowHomeActivityTestFixture)
}
