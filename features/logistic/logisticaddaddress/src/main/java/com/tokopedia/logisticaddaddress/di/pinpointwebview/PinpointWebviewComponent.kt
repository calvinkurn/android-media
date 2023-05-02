package com.tokopedia.logisticaddaddress.di.pinpointwebview

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticaddaddress.features.pinpoint.webview.PinpointWebviewFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [PinpointWebviewModule::class, PinpointWebviewViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface PinpointWebviewComponent {
    fun inject(pinpointWebviewFragment: PinpointWebviewFragment)
}
