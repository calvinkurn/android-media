package com.tokopedia.accountprofile.changephonenumber.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.accountprofile.changephonenumber.di.module.ChangePhoneNumberModule
import com.tokopedia.accountprofile.changephonenumber.di.module.ChangePhoneNumberUseCaseModule
import com.tokopedia.accountprofile.changephonenumber.di.module.ChangePhoneNumberViewModelModule
import com.tokopedia.accountprofile.changephonenumber.features.ChangePhoneNumberActivity
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
