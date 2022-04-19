package com.tokopedia.managepassword.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.managepassword.addpassword.view.activity.AddPasswordActivity
import com.tokopedia.managepassword.addpassword.view.fragment.AddPasswordFragment
import com.tokopedia.managepassword.di.module.ManagePasswordModule
import com.tokopedia.managepassword.di.module.ManagePasswordUseCaseModule
import com.tokopedia.managepassword.di.module.ManagePasswordViewModelModule
import com.tokopedia.managepassword.forgotpassword.view.activity.ForgotPasswordActivity
import com.tokopedia.managepassword.haspassword.view.activity.HasPasswordActivity
import dagger.Component

@ActivityScope
@Component(
    modules = [
        ManagePasswordModule::class,
        ManagePasswordUseCaseModule::class,
        ManagePasswordViewModelModule::class
    ], dependencies = [
        BaseAppComponent::class
    ]
)
interface ManagePasswordComponent {
    fun inject(activity: HasPasswordActivity)
    fun inject(activity: AddPasswordActivity)
    fun inject(activity: ForgotPasswordActivity)
    fun inject(fragment: AddPasswordFragment)
}