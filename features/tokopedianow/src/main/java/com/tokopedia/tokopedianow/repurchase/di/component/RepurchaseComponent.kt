package com.tokopedia.tokopedianow.repurchase.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.repurchase.di.module.RepurchaseModule
import com.tokopedia.tokopedianow.repurchase.di.module.RepurchaseViewModelModule
import com.tokopedia.tokopedianow.repurchase.di.scope.RepurchaseScope
import com.tokopedia.tokopedianow.repurchase.presentation.fragment.TokoNowRepurchaseFragment
import dagger.Component

@RepurchaseScope
@Component(
    modules = [
        RepurchaseModule::class,
        RepurchaseViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface RepurchaseComponent {

    fun inject(fragment: TokoNowRepurchaseFragment)
}