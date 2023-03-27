package com.tokopedia.minicart.common.widget.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.minicart.common.simplified.MiniCartSimplifiedWidget
import com.tokopedia.minicart.common.widget.MiniCartWidget
import com.tokopedia.minicart.common.widget.general.MiniCartGeneralWidget
import dagger.Component

@ActivityScope
@Component(modules = [MiniCartWidgetViewModelModule::class, MiniCartWidgetModule::class], dependencies = [BaseAppComponent::class])
interface MiniCartWidgetComponent {
    fun inject(miniCartWidget: MiniCartWidget)
    fun inject(miniCartGeneralWidget: MiniCartGeneralWidget)
    fun inject(miniCartSimplifiedWidget: MiniCartSimplifiedWidget)
}
