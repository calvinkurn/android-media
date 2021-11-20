package com.tokopedia.cmhomewidget.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.cmhomewidget.di.module.DummyTestCMHomeWidgetModule
import com.tokopedia.cmhomewidget.di.module.DummyTestCMHomeWidgetViewModelModule
import com.tokopedia.cmhomewidget.di.scope.DummyTestCMHomeWidgetActivityScope
import com.tokopedia.cmhomewidget.presentation.activity.DummyTestCMHomeWidgetActivity
import dagger.Component

// todo delete cm home widget dummy things

@DummyTestCMHomeWidgetActivityScope
@Component(
    modules = [DummyTestCMHomeWidgetViewModelModule::class, DummyTestCMHomeWidgetModule::class],
    dependencies = [BaseAppComponent::class]
)
interface DummyTestCMHomeWidgetComponent {

    fun inject(dummyTestCMHomeWidgetActivity: DummyTestCMHomeWidgetActivity)
}