package com.tokopedia.analyticsdebugger.serverlogger.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.analyticsdebugger.serverlogger.di.module.ServerLoggerModule
import com.tokopedia.analyticsdebugger.serverlogger.di.scope.ServerLoggerScope
import com.tokopedia.analyticsdebugger.serverlogger.presentation.fragment.ServerLoggerFragment
import dagger.Component

@ServerLoggerScope
@Component(
    modules = [ServerLoggerModule::class],
    dependencies = [BaseAppComponent::class]
)
interface ServerLoggerComponent {
    fun inject(serverLoggerFragment: ServerLoggerFragment)
}