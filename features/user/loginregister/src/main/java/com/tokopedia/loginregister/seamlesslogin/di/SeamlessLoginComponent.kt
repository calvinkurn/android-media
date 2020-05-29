package com.tokopedia.loginregister.seamlesslogin.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.seamlesslogin.RemoteService
import com.tokopedia.sessioncommon.di.SessionCommonScope
import com.tokopedia.sessioncommon.di.SessionModule
import dagger.Component

/**
 * @author by nisie on 10/25/18.
 */
@SeamlessLoginScope
@Component(modules = [
    SeamlessLoginModule::class,
    SeamlessLoginQueryModule::class,
    SeamlessLoginUseCaseModule::class,
    SeamlessLoginViewModelModule::class
], dependencies = [LoginRegisterComponent::class])
interface SeamlessLoginComponent {
    fun inject(service: RemoteService)
}