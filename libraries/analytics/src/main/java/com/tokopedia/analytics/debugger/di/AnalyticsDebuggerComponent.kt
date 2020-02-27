package com.tokopedia.analytics.debugger.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.analytics.debugger.ui.AnalyticsDebugger
import com.tokopedia.analytics.debugger.ui.fragment.AnalyticsDebuggerFragment
import com.tokopedia.analytics.debugger.ui.fragment.AnalyticsDebuggerGtmErrorFragment
import com.tokopedia.analytics.debugger.ui.fragment.FpmDebuggerFragment
import dagger.Component
import javax.inject.Named

/**
 * @author okasurya on 5/17/18.
 */
@Component(modules = [AnalyticsDebuggerModule::class], dependencies = [BaseAppComponent::class])
@AnalyticsDebuggerScope
interface AnalyticsDebuggerComponent {
    fun inject(fragment: AnalyticsDebuggerFragment?)
    fun inject(fragment: FpmDebuggerFragment?)
    fun inject(fragment: AnalyticsDebuggerGtmErrorFragment?)

    @get:Named(NAMED_GTM_ANALYTICS)
    val gtmPresenter: AnalyticsDebugger.Presenter

    @get:Named(NAMED_FPM_ANALYTICS)
    val fpmPresenter: AnalyticsDebugger.Presenter

    @get:Named(NAMED_GTM_ERROR_ANALYTICS)
    val gtmErrorPresenter: AnalyticsDebugger.Presenter

    @get:Named(NAMED_IRIS_SAVE)
    val gtmIrisSavePresenter: AnalyticsDebugger.Presenter

    @get:Named(NAMED_IRIS_SEND)
    val gtmIrisSendPresenter: AnalyticsDebugger.Presenter
}