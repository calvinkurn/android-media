package com.tokopedia.changephonenumber.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.changephonenumber.di.module.ChangePhoneNumberModule
import com.tokopedia.changephonenumber.di.module.ChangePhoneNumberUseCaseModule
import com.tokopedia.changephonenumber.di.module.ChangePhoneNumberViewModelModule
import com.tokopedia.changephonenumber.features.ChangePhoneNumberActivity
import dagger.Component

@ActivityScope
@Component(modules = [
    ChangePhoneNumberModule::class,
    ChangePhoneNumberUseCaseModule::class,
    ChangePhoneNumberViewModelModule::class
], dependencies = [
    BaseAppComponent::class
])
interface ChangePhoneNumberComponent {
    fun inject(changePhoneNumberActivity: ChangePhoneNumberActivity)
}