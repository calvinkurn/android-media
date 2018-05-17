package com.tokopedia.analytics.debugger.di;

import com.tokopedia.analytics.debugger.domain.GetGtmLogUseCase;
import com.tokopedia.analytics.debugger.ui.AnalyticsDebugger;
import com.tokopedia.analytics.debugger.ui.presenter.AnalyticsDebuggerPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author okasurya on 5/17/18.
 */

@Module
@AnalyticsDebuggerScope
public class AnalyticsDebuggerModule {
    @Provides
    AnalyticsDebugger.Presenter providePresenter(GetGtmLogUseCase getGtmLogUseCase) {
        return new AnalyticsDebuggerPresenter(getGtmLogUseCase);
    }
}
