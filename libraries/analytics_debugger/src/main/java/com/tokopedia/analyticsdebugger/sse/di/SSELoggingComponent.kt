package com.tokopedia.analyticsdebugger.sse.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.analyticsdebugger.sse.ui.fragment.SSELoggingFragment
import dagger.Component

/**
 * Created By : Jonathan Darwin on November 10, 2021
 */
@Component(
    modules = [SSELoggingModule::class, SSELoggingViewModelModule::class, SSELoggingBindModule::class],
    dependencies = [BaseAppComponent::class]
)
@SSELoggingScope
interface SSELoggingComponent {

    fun inject(sseLoggingFragment: SSELoggingFragment)
}