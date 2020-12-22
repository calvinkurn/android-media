package com.tokopedia.loginregister.login.di

import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.login.service.RegisterPushNotifService
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import com.tokopedia.loginregister.login.view.fragment.LoginEmailPhoneFragment
import com.tokopedia.loginregister.login.view.fragment.SellerSeamlessLoginFragment
import dagger.Component

/**
 * @author by nisie on 10/15/18.
 */
@LoginScope
@Component(modules = [
    LoginModule::class,
    LoginQueryModule::class,
    LoginUseCaseModule::class,
    SeamlessSellerViewModelModule::class
], dependencies = [LoginRegisterComponent::class])
interface LoginComponent {
    fun inject(activity: LoginActivity)
    fun inject(fragment: LoginEmailPhoneFragment)
    fun inject(fragment: SellerSeamlessLoginFragment)
    fun inject(service: RegisterPushNotifService)
}