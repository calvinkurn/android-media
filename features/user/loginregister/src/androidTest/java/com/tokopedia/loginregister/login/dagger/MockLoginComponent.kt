package com.tokopedia.loginregister.login.dagger

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.login.di.*
import dagger.Component

/**
 * Created by Yoris Prayogo on 09/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
@LoginScope
@Component(modules = [
    MockLoginmodule::class,
    MockLoginQueryModule::class,
    MockLoginUseCaseModule::class,
    MockSeamlessViewModelModule::class
], dependencies = [LoginRegisterComponent::class])
interface MockLoginComponent: LoginComponent {}