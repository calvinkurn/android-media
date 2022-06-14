package com.tokopedia.oldminicart.common.widget.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.oldminicart.common.widget.MiniCartWidget
import dagger.Component

@ActivityScope
@Component(modules = [MiniCartWidgetViewModelModule::class, MiniCartWidgetModule::class], dependencies = [BaseAppComponent::class])
interface MiniCartWidgetComponent {
    fun inject(miniCartWidget: MiniCartWidget)
}