package com.tokopedia.gopayhomewidget.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.gopayhomewidget.di.module.PaylaterHomeWidgetModule
import com.tokopedia.gopayhomewidget.di.scope.PaylaterHomeWidget
import com.tokopedia.gopayhomewidget.presentation.PayLaterWidget
import dagger.Component

@PaylaterHomeWidget
@Component(
        modules =
        [PaylaterHomeWidgetModule::class],
        dependencies = [BaseAppComponent::class]
)
interface PayLaterHomeWidgetComponent {

    fun inject(paylaterWidget: PayLaterWidget)

}