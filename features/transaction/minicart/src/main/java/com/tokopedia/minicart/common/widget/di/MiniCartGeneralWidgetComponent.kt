package com.tokopedia.minicart.common.widget.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.minicart.common.general.MiniCartGeneralWidget
import com.tokopedia.minicart.common.simplified.MiniCartSimplifiedWidget
import dagger.Component

@ActivityScope
@Component(
    modules = [MiniCartGeneralWidgetModule::class, MiniCartGeneralWidgetViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface MiniCartGeneralWidgetComponent {

    fun inject(miniCartGeneralWidget: MiniCartGeneralWidget)

    fun inject(miniCartSimplifiedWidget: MiniCartSimplifiedWidget)
}