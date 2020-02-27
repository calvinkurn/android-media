package com.tokopedia.analytics.debugger.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.analytics.debugger.domain.*
import com.tokopedia.analytics.debugger.ui.AnalyticsDebugger
import com.tokopedia.analytics.debugger.ui.presenter.*
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * @author okasurya on 5/17/18.
 */
@Module
@AnalyticsDebuggerScope
class AnalyticsDebuggerModule {
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Named(NAMED_GTM_ANALYTICS)
    fun providePresenter(getGtmLogUseCase: GetGtmLogUseCase?,
                         deleteGtmLogUseCase: DeleteGtmLogUseCase?): AnalyticsDebugger.Presenter {
        return AnalyticsDebuggerPresenter(getGtmLogUseCase, deleteGtmLogUseCase)
    }

    @Provides
    @Named(NAMED_FPM_ANALYTICS)
    fun providePresenter(getFpmLogUseCase: GetFpmLogUseCase?,
                         deleteFpmLogUseCase: DeleteFpmLogUseCase?): AnalyticsDebugger.Presenter {
        return FpmDebuggerPresenter(getFpmLogUseCase, deleteFpmLogUseCase)
    }

    @Provides
    @Named(NAMED_GTM_ERROR_ANALYTICS)
    fun provideGtmErrorPresenter(getGtmErrorLogUseCase: GetGtmErrorLogUseCase,
                                 deleteGtmErrorLogUseCase: DeleteGtmErrorLogUseCase): AnalyticsDebugger.Presenter {
        return AnalyticsGtmErrorDebuggerPresenter(getGtmErrorLogUseCase, deleteGtmErrorLogUseCase)
    }

    @Provides
    @Named(NAMED_IRIS_SAVE)
    fun provideIrisSavePresenter(getIrisSaveLogUseCase: GetIrisSaveLogUseCase,
                                 deleteIrisSaveLogUseCase: DeleteIrisSaveLogUseCase): AnalyticsDebugger.Presenter {
        return AnalyticsIrisSaveDebuggerPresenter(getIrisSaveLogUseCase, deleteIrisSaveLogUseCase)
    }

    @Provides
    @Named(NAMED_IRIS_SEND)
    fun provideIrisSendPresenter(getIrisSendLogUseCase: GetIrisSendLogUseCase,
                                 deleteIrisSendLogUseCase: DeleteIrisSendLogUseCase): AnalyticsDebugger.Presenter {
        return AnalyticsIrisSendDebuggerPresenter(getIrisSendLogUseCase, deleteIrisSendLogUseCase)
    }


}