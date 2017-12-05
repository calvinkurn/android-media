package com.tokopedia.tkpd.home.thankyou.di.module;

import com.tokopedia.tkpd.home.thankyou.di.scope.ThanksAnalyticsScope;
import com.tokopedia.tkpd.home.thankyou.view.ThanksAnalytics;
import com.tokopedia.tkpd.home.thankyou.view.presenter.ThanksAnalyticsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by okasurya on 12/4/17.
 */
@Module
@ThanksAnalyticsScope
public class ThanksAnalyticsModule {
    @Provides
    @ThanksAnalyticsScope
    ThanksAnalytics.Presenter provideThanksAnalyticsPresenter() {
        return new ThanksAnalyticsPresenter();
    }
}
