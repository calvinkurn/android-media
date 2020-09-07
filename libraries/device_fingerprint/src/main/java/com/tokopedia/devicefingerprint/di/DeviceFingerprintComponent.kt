package com.tokopedia.devicefingerprint.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

@DeviceFingerprintScope
@Component(dependencies = [BaseAppComponent::class])
interface DeviceFingerprintComponent {
}
