package com.tokopedia.cmhomewidget.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.cmhomewidget.di.CMHomeWidgetScope
import com.tokopedia.cmhomewidget.di.module.CMHomeWidgetModule
import com.tokopedia.cmhomewidget.di.module.ViewModelModule
import com.tokopedia.cmhomewidget.presentation.activity.DummyTestCMHomeWidgetActivity

import dagger.Component

@CMHomeWidgetScope
@Component(
    modules = [ViewModelModule::class,CMHomeWidgetModule::class],
    dependencies = [BaseAppComponent::class]
)
interface CMHomeWidgetComponent {

    fun inject(dummyTestCMHomeWidgetActivity: DummyTestCMHomeWidgetActivity)

}