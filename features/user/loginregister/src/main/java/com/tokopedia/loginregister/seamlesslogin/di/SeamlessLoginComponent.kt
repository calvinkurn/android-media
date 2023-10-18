package com.tokopedia.loginregister.seamlesslogin.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.loginregister.seamlesslogin.RemoteService
import dagger.Component

/**
 * @author by nisie on 10/25/18.
 */
@ActivityScope
@Component(modules = [
    SeamlessLoginModule::class,
    SeamlessLoginViewModelModule::class
], dependencies = [BaseAppComponent::class])
interface SeamlessLoginComponent {
    fun inject(service: RemoteService)
}
