package com.tokopedia.cmhomewidget.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.cmhomewidget.di.module.CMHomeWidgetModule
import com.tokopedia.cmhomewidget.di.scope.CMHomeWidgetScope
import com.tokopedia.cmhomewidget.presentation.customview.CMHomeWidget
import dagger.Component

@CMHomeWidgetScope
@Component(
    modules = [CMHomeWidgetModule::class],
    dependencies = [BaseAppComponent::class]
)
interface CMHomeWidgetComponent {

    fun inject(cmHomeWidget: CMHomeWidget)
}