package com.tokopedia.loginregister.registerinitial.di

import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterEmailFragment
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterInitialFragment
import dagger.Component

/**
 * @author by nisie on 10/25/18.
 */
@RegisterInitialScope
@Component(modules = [
    RegisterInitialModule::class,
    RegisterInitialQueryModule::class,
    RegisterInitialUseCaseModule::class,
    RegisterInitialViewModelModule::class
], dependencies = [LoginRegisterComponent::class])
interface RegisterInitialComponent {
    fun inject(fragment: RegisterInitialFragment?)
    fun inject(fragment: RegisterEmailFragment?)
}