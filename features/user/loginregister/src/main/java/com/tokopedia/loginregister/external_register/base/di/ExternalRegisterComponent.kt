package com.tokopedia.loginregister.external_register.base.di

import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.external_register.base.fragment.ExternalAccountFinalFragment
import com.tokopedia.loginregister.external_register.ovo.view.fragment.OvoAddNameFragment
import com.tokopedia.loginregister.external_register.ovo.view.fragment.OvoRegisterInitialFragment
import dagger.Component

/**
 * Created by Yoris Prayogo on 19/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@ExternalRegisterScope
@Component(modules = [
    ExternalRegisterModules::class,
    ExternalRegisterUseCaseModules::class,
    ExternalRegisterUseCaseViewModelModules::class
], dependencies = [LoginRegisterComponent::class])
interface ExternalRegisterComponent {
    fun inject(fragment: OvoAddNameFragment)
    fun inject(fragment: OvoRegisterInitialFragment)
    fun inject(fragment: ExternalAccountFinalFragment)
}
