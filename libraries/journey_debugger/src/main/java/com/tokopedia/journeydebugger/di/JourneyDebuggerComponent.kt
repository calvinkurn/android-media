package com.tokopedia.analyticsdebugger.debugger.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.analytics.debugger.ui.fragment.ApplinkDebuggerFragment
import com.tokopedia.analytics.debugger.ui.fragment.FpmDebuggerFragment
import com.tokopedia.analytics.debugger.ui.fragment.TopAdsDebuggerFragment
import com.tokopedia.analyticsdebugger.debugger.ui.AnalyticsDebugger
import com.tokopedia.analyticsdebugger.debugger.ui.presenter.ApplinkDebugger
import com.tokopedia.analyticsdebugger.debugger.ui.presenter.FpmDebugger
import com.tokopedia.analyticsdebugger.debugger.ui.presenter.TopAdsDebugger
import dagger.Component
import javax.inject.Named

@Component(modules = [JourneyDebuggerModule::class], dependencies = [BaseAppComponent::class])
@JourneyDebuggerScope
interface JourneyDebuggerComponent {
    fun inject(fragment: JourneyDebuggerFragment?)

    @get:Named(NAMED_JOURNEY)
    val journeyPresenter: JourneyDebugger.Presenter
}
