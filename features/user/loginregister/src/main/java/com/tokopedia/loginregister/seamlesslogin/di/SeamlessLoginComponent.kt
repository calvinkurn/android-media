package com.tokopedia.loginregister.seamlesslogin.di

import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.seamlesslogin.ui.RemoteService
import dagger.Component

/**
 * @author by nisie on 10/25/18.
 */
@SeamlessLoginScope
@Component(modules = [
    SeamlessLoginModule::class,
    SeamlessLoginUseCaseModule::class,
    SeamlessLoginViewModelModule::class
], dependencies = [LoginRegisterComponent::class])
interface SeamlessLoginComponent {
    fun inject(service: RemoteService)
}
