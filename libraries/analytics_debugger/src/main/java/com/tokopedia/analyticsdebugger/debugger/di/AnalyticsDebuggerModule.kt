package com.tokopedia.analyticsdebugger.debugger.di

import com.tokopedia.analyticsdebugger.debugger.domain.*
import com.tokopedia.analyticsdebugger.debugger.ui.AnalyticsDebugger
import com.tokopedia.analyticsdebugger.debugger.ui.presenter.*
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * @author okasurya on 5/17/18.
 */
@Module
class AnalyticsDebuggerModule {

    @Provides
    fun provideFpmPresenter(
        getFpmLogUseCase: GetFpmLogUseCase,
        deleteFpmLogUseCase: DeleteFpmLogUseCase,
        getFpmAllDataUseCase: GetFpmAllDataUseCase
    ): FpmDebugger.Presenter {
        return FpmDebuggerPresenter(getFpmLogUseCase, deleteFpmLogUseCase, getFpmAllDataUseCase)
    }

    @Provides
    fun provideApplinkPresenter(
        getApplinkLogUseCase: GetApplinkLogUseCase,
        deleteApplinkLogUseCase: DeleteApplinkLogUseCase
    ): ApplinkDebugger.Presenter {
        return ApplinkDebuggerPresenter(getApplinkLogUseCase, deleteApplinkLogUseCase)
    }

    @Provides
    fun provideTopAdsPresenter(
        getTopAdsLogUseCase: GetTopAdsLogUseCase,
        deleteTopAdsLogUseCase: DeleteTopAdsLogUseCase
    ): TopAdsDebugger.Presenter {
        return TopAdsDebuggerPresenter(getTopAdsLogUseCase, deleteTopAdsLogUseCase)
    }

    @Provides
    @Named(NAMED_IRIS_SAVE)
    fun provideIrisSavePresenter(
        getIrisSaveLogUseCase: GetIrisSaveLogUseCase,
        getIrisSaveCountLogUseCase: GetIrisSaveCountLogUseCase,
        deleteIrisSaveLogUseCase: DeleteIrisSaveLogUseCase
    ): AnalyticsDebugger.Presenter {
        return AnalyticsIrisSaveDebuggerPresenter(getIrisSaveLogUseCase, getIrisSaveCountLogUseCase, deleteIrisSaveLogUseCase)
    }

    @Provides
    @Named(NAMED_IRIS_SEND)
    fun provideIrisSendPresenter(
        getIrisSendLogUseCase: GetIrisSendLogUseCase,
        getIrisSendCountLogUseCase: GetIrisSendCountLogUseCase,
        deleteIrisSendLogUseCase: DeleteIrisSendLogUseCase
    ): AnalyticsDebugger.Presenter {
        return AnalyticsIrisSendDebuggerPresenter(getIrisSendLogUseCase, getIrisSendCountLogUseCase, deleteIrisSendLogUseCase)
    }

    @Provides
    fun provideServerLogPresenter(
        getServerLogUseCase: GetServerLogUseCase,
        deleteServerLogUseCase: DeleteServerLogUseCase
    ): AnalyticsServerLogDebuggerPresenter {
        return AnalyticsServerLogDebuggerPresenter(getServerLogUseCase, deleteServerLogUseCase)
    }
}
