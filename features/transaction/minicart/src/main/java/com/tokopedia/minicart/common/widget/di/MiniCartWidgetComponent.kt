package com.tokopedia.minicart.common.widget.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.minicart.common.widget.MiniCartWidget
import dagger.Component

@MiniCartWidgetScope
@Component(modules = [MiniCartWidgetViewModelModule::class, MiniCartWidgetModule::class], dependencies = [BaseAppComponent::class])
interface MiniCartWidgetComponent {
    fun inject(miniCartWidget: MiniCartWidget)
}