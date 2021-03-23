package com.tokopedia.loginregister.login.di

import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.login.service.GetDefaultChosenAddressService
import com.tokopedia.loginregister.login.service.RegisterPushNotifService
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import com.tokopedia.loginregister.login.view.fragment.LoginEmailPhoneFragment
import com.tokopedia.loginregister.login.view.fragment.SellerSeamlessLoginFragment
import dagger.Component

/**
 * Created by Yoris Prayogo on 24/03/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */

@LoginScope
@Component(modules = [
    LoginModule::class,
    LoginQueryModule::class,
    LoginUseCaseModule::class,
    LoginViewModelModule::class
], dependencies = [LoginRegisterComponent::class])
interface LoginComponent {
    fun inject(activity: LoginActivity)
    fun inject(fragment: LoginEmailPhoneFragment)
    fun inject(fragment: SellerSeamlessLoginFragment)
    fun inject(service: RegisterPushNotifService)
    fun inject(getDefaultChosenAddressService: GetDefaultChosenAddressService?)

}