package com.tokopedia.analyticsdebugger.debugger.di

import android.content.Context
import com.tokopedia.analytics.debugger.ui.fragment.ApplinkDebuggerFragment
import com.tokopedia.analytics.debugger.ui.fragment.FpmDebuggerFragment
import com.tokopedia.analytics.debugger.ui.fragment.TopAdsDebuggerFragment
import com.tokopedia.analyticsdebugger.debugger.ui.AnalyticsDebugger
import com.tokopedia.analyticsdebugger.debugger.ui.fragment.ServerLogDebuggerFragment
import com.tokopedia.analyticsdebugger.debugger.ui.presenter.AnalyticsServerLogDebuggerPresenter
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton

/**
 * @author okasurya on 5/17/18.
 */
@Component(modules = [AnalyticsDebuggerModule::class])
@Singleton
interface AnalyticsDebuggerComponent {
    fun inject(fragment: FpmDebuggerFragment?)
    fun inject(fragment: ApplinkDebuggerFragment?)
    fun inject(fragment: TopAdsDebuggerFragment?)

    @get:Named(NAMED_IRIS_SAVE)
    val gtmIrisSavePresenter: AnalyticsDebugger.Presenter

    @get:Named(NAMED_IRIS_SEND)
    val gtmIrisSendPresenter: AnalyticsDebugger.Presenter

    val serverLoggerpresenter: AnalyticsServerLogDebuggerPresenter

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AnalyticsDebuggerComponent
    }
}
