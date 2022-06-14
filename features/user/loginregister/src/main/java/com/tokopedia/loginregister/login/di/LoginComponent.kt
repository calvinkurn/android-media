package com.tokopedia.loginregister.login.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.loginregister.common.di.LoginRegisterModule
import com.tokopedia.loginregister.common.di.LoginRegisterScope
import com.tokopedia.loginregister.login.service.GetDefaultChosenAddressService
import com.tokopedia.loginregister.login.service.RegisterPushNotifService
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import com.tokopedia.loginregister.login.view.bottomsheet.NeedHelpBottomSheet
import com.tokopedia.loginregister.login.view.fragment.LoginEmailPhoneFragment
import com.tokopedia.loginregister.login.view.fragment.SellerSeamlessLoginFragment
import com.tokopedia.sessioncommon.di.SessionCommonScope
import com.tokopedia.sessioncommon.di.SessionModule
import dagger.Component

/**
 * Created by Yoris Prayogo on 24/03/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */

@LoginRegisterScope
@SessionCommonScope
@LoginScope
@Component(modules = [
    LoginModule::class,
    LoginQueryModule::class,
    LoginUseCaseModule::class,
    LoginViewModelModule::class,
    LoginRegisterModule::class,
    SessionModule::class,
], dependencies = [BaseAppComponent::class])
interface LoginComponent {
    fun inject(activity: LoginActivity)
    fun inject(fragment: LoginEmailPhoneFragment)
    fun inject(fragment: SellerSeamlessLoginFragment)
    fun inject(service: RegisterPushNotifService)
    fun inject(getDefaultChosenAddressService: GetDefaultChosenAddressService?)
    fun inject(bottomSheet: NeedHelpBottomSheet)
}