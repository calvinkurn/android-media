package com.tokopedia.rechargeocr.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.rechargeocr.RechargeCameraFragment
import dagger.Component

@RechargeCameraScope
@Component(modules = [RechargeCameraModule::class, RechargeCameraModelModule::class],
        dependencies = [BaseAppComponent::class])
interface RechargeCameraComponent {

    fun inject(rechargeCameraFragment: RechargeCameraFragment)

}