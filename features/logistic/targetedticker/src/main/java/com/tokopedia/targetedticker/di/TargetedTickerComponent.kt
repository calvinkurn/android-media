package com.tokopedia.targetedticker.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.targetedticker.ui.TargetedTickerWidget
import dagger.Component

/**
 * Created by irpan on 09/10/23.
 */
@ActivityScope
@Component(
    modules = [
        TargetedTickerModule::class,
        TargetedTickerViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface TargetedTickerComponent {
    fun inject(widget: TargetedTickerWidget)
}
