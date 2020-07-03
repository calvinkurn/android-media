package com.tokopedia.managepassword.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.managepassword.di.module.ManagePasswordModule
import com.tokopedia.managepassword.di.module.ManagePasswordUseCaseModule
import com.tokopedia.managepassword.di.module.ManagePasswordViewModelModule
import com.tokopedia.managepassword.view.activity.HasPasswordActivity
import dagger.Component

@ManagePasswordScope
@Component(modules = [
    ManagePasswordModule::class,
    ManagePasswordUseCaseModule::class,
    ManagePasswordViewModelModule::class
], dependencies = [
    BaseAppComponent::class
])
interface ManagePasswordComponent {
    fun inject(activity: HasPasswordActivity)
}