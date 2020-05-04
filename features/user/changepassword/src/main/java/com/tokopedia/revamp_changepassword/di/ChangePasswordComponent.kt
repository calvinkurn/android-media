package com.tokopedia.revamp_changepassword.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.revamp_changepassword.di.module.ChangePasswordModule
import com.tokopedia.revamp_changepassword.di.module.ChangePasswordUseCaseModule
import com.tokopedia.revamp_changepassword.di.module.ChangePasswordViewModelModule
import com.tokopedia.revamp_changepassword.view.activity.HasPasswordActivity
import dagger.Component

@ChangePasswordScope
@Component(modules = [
    ChangePasswordModule::class,
    ChangePasswordUseCaseModule::class,
    ChangePasswordViewModelModule::class
], dependencies = [
    BaseAppComponent::class
])
interface ChangePasswordComponent {
    fun inject(activity: HasPasswordActivity)
}