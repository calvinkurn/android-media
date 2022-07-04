package com.tokopedia.usercomponents.tokopediaplus.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.usercomponents.tokopediaplus.ui.TokopediaPlusWidget
import dagger.Component

@ActivityScope
@Component(modules = [
    TokopediaPlusModule::class,
    TokopediaPlusViewModelModule::class
], dependencies = [BaseAppComponent::class])
interface TokopediaPlusComponent {
    fun inject(tokopediaPlusWidget: TokopediaPlusWidget)
}