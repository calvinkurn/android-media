package com.scp.auth.di

import com.scp.auth.authentication.ScpAuthActivity
import com.scp.auth.registerpushnotif.services.ScpRegisterPushNotificationWorker
import com.scp.auth.service.GetDefaultChosenAddressService
import com.scp.auth.verification.IVerificationSdk
import com.scp.auth.verification.ScpVerificationActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import dagger.Component

@ActivityScope
@Component(modules = [ScpModules::class, ScpAuthViewModelModule::class], dependencies = [BaseAppComponent::class])
interface ScpAuthComponent {
    fun inject(fragment: ScpAuthActivity)

    fun inject(service: GetDefaultChosenAddressService)
    fun inject(service: ScpRegisterPushNotificationWorker)
    fun inject(verificationSdk: IVerificationSdk)
    fun inject(actvity: ScpVerificationActivity)
}
