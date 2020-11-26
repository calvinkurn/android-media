package com.tokopedia.loginregister.registerinitial.di

import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import dagger.Component

@RegisterInitialScope
@Component(modules = [
    MockRegisterInitialModule::class,
    MockRegisterInitialQueryModule::class,
    MockRegisterInitialUseCaseModule::class,
    MockRegisterInitialViewModelModule::class],
dependencies = [LoginRegisterComponent::class])
interface MockRegisterInitialComponent {}