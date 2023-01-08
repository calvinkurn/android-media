package com.tokopedia.loginregister.login.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.loginregister.login.service.GetDefaultChosenAddressService
import com.tokopedia.loginregister.login.service.RegisterPushNotifService
import com.tokopedia.loginregister.login.view.bottomsheet.NeedHelpBottomSheet
import com.tokopedia.loginregister.login.view.fragment.LoginEmailPhoneFragment
import com.tokopedia.loginregister.login.view.fragment.SellerSeamlessLoginFragment
import dagger.Component

/**
 * Created by Yoris Prayogo on 24/03/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */

@ActivityScope
@Component(modules = [
    LoginModule::class,
    LoginUseCaseModule::class,
    LoginViewModelModule::class,
], dependencies = [BaseAppComponent::class])
interface LoginComponent {
    fun inject(fragment: LoginEmailPhoneFragment)
    fun inject(fragment: SellerSeamlessLoginFragment)
    fun inject(service: RegisterPushNotifService)
    fun inject(getDefaultChosenAddressService: GetDefaultChosenAddressService?)
    fun inject(bottomSheet: NeedHelpBottomSheet)
}