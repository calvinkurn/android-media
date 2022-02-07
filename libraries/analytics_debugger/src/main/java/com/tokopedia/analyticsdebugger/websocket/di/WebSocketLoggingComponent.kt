package com.tokopedia.analyticsdebugger.websocket.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.analyticsdebugger.websocket.ui.fragment.WebSocketLoggingFragment
import dagger.Component

/**
 * Created By : Jonathan Darwin on December 20, 2021
 */

@WebSocketLoggingScope
@Component(
    modules = [WebSocketLoggingBindModule::class, WebSocketLoggingModule::class, WebSocketLoggingViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface WebSocketLoggingComponent {

    fun inject(fragment: WebSocketLoggingFragment)
}