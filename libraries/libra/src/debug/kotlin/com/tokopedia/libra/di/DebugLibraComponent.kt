package com.tokopedia.libra.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.libra.DebugLibraActivity
import dagger.Component

@LibraScope
@Component(
    modules = [DebugLibraModule::class],
    dependencies = [BaseAppComponent::class]
)
interface DebugLibraComponent {
    fun inject(activity: DebugLibraActivity)
}
