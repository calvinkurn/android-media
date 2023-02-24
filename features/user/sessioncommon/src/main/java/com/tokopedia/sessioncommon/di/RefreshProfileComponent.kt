package com.tokopedia.sessioncommon.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.sessioncommon.worker.RefreshProfileWorker
import dagger.Component

@ActivityScope
@Component(modules = [RefreshProfileModule::class], dependencies = [BaseAppComponent::class])
interface RefreshProfileComponent {
    fun inject(worker: RefreshProfileWorker)
}
