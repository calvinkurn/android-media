package com.tokopedia.libra.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.libra.LibraInstance
import dagger.Component

@LibraScope
@Component(
    modules = [LibraModule::class],
    dependencies = [BaseAppComponent::class]
)
interface LibraComponent {
    fun inject(`this`: LibraInstance)
}
