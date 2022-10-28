package com.tokopedia.loginregister.goto_seamless.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.loginregister.goto_seamless.GotoSeamlessLoginFragment
import com.tokopedia.loginregister.goto_seamless.worker.TemporaryTokenWorker
import dagger.Component

@ActivityScope
@Component(modules = [
    GotoSeamlessModules::class,
    GotoSeamlessViewModelModules::class
], dependencies = [BaseAppComponent::class])
interface GotoSeamlessComponent {
    fun inject(fragment: GotoSeamlessLoginFragment)
    fun inject(temporaryTokenWorker: TemporaryTokenWorker)
}
