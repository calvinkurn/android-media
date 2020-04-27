package com.tokopedia.analyticsdebugger.debugger.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.analyticsdebugger.debugger.ui.AnalyticsDebugger
import com.tokopedia.analyticsdebugger.debugger.ui.fragment.AnalyticsDebuggerFragment
import com.tokopedia.analyticsdebugger.debugger.ui.fragment.AnalyticsDebuggerGtmErrorFragment
import dagger.Component
import javax.inject.Named

/**
 * @author okasurya on 5/17/18.
 */
@Component(modules = [AnalyticsDebuggerModule::class], dependencies = [BaseAppComponent::class])
@AnalyticsDebuggerScope
interface AnalyticsDebuggerComponent {
    fun inject(fragment: AnalyticsDebuggerFragment?)
    fun inject(fragment: AnalyticsDebuggerGtmErrorFragment?)

    @get:Named(NAMED_GTM_ANALYTICS)
    val gtmPresenter: AnalyticsDebugger.Presenter

    @get:Named(NAMED_GTM_ERROR_ANALYTICS)
    val gtmErrorPresenter: AnalyticsDebugger.Presenter

    @get:Named(NAMED_IRIS_SAVE)
    val gtmIrisSavePresenter: AnalyticsDebugger.Presenter

    @get:Named(NAMED_IRIS_SEND)
    val gtmIrisSendPresenter: AnalyticsDebugger.Presenter
}