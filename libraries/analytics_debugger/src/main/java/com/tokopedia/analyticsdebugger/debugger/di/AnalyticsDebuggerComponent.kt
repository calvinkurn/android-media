package com.tokopedia.analyticsdebugger.debugger.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.analytics.debugger.ui.fragment.ApplinkDebuggerFragment
import com.tokopedia.analytics.debugger.ui.fragment.FpmDebuggerFragment
import com.tokopedia.analytics.debugger.ui.fragment.TopAdsDebuggerFragment
import com.tokopedia.analyticsdebugger.debugger.ui.AnalyticsDebugger
import com.tokopedia.analyticsdebugger.cassava.debugger.AnalyticsDebuggerFragment
import com.tokopedia.analyticsdebugger.debugger.ui.presenter.ApplinkDebugger
import com.tokopedia.analyticsdebugger.debugger.ui.presenter.FpmDebugger
import com.tokopedia.analyticsdebugger.debugger.ui.presenter.TopAdsDebugger
import dagger.Component
import javax.inject.Named

/**
 * @author okasurya on 5/17/18.
 */
@Component(modules = [AnalyticsDebuggerModule::class], dependencies = [BaseAppComponent::class])
@AnalyticsDebuggerScope
interface AnalyticsDebuggerComponent {
    fun inject(fragment: FpmDebuggerFragment?)
    fun inject(fragment: ApplinkDebuggerFragment?)
    fun inject(fragment: TopAdsDebuggerFragment?)

    @get:Named(NAMED_FPM_ANALYTICS)
    val fpmPresenter: FpmDebugger.Presenter

    @get:Named(NAMED_APPLINK)
    val applinkPresenter: ApplinkDebugger.Presenter

    @get:Named(NAMED_TOPADS)
    val topAdsPresenter: TopAdsDebugger.Presenter

    @get:Named(NAMED_IRIS_SAVE)
    val gtmIrisSavePresenter: AnalyticsDebugger.Presenter

    @get:Named(NAMED_IRIS_SEND)
    val gtmIrisSendPresenter: AnalyticsDebugger.Presenter
}