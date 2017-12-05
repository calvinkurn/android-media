package com.tokopedia.tkpd.home.thankyou.di.module;

import com.google.gson.Gson;
import com.tokopedia.core.network.di.qualifier.RechargeDigitalAuthQualifier;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.home.thankyou.data.factory.ThanksAnalyticsFactory;
import com.tokopedia.tkpd.home.thankyou.data.repository.ThanksAnalyticsRepository;
import com.tokopedia.tkpd.home.thankyou.data.repository.ThanksAnalyticsRepositoryImpl;
import com.tokopedia.tkpd.home.thankyou.data.source.api.DigitalThanksApi;
import com.tokopedia.tkpd.home.thankyou.di.scope.ThanksAnalyticsScope;
import com.tokopedia.tkpd.home.thankyou.domain.usecase.ThanksAnalyticsUseCase;
import com.tokopedia.tkpd.home.thankyou.view.ThanksAnalytics;
import com.tokopedia.tkpd.home.thankyou.view.presenter.ThanksAnalyticsPresenter;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by okasurya on 12/4/17.
 */
@Module
@ThanksAnalyticsScope
public class ThanksAnalyticsModule {
    @Provides
    @ThanksAnalyticsScope
    DigitalThanksApi digitalThanksApi(@RechargeDigitalAuthQualifier Retrofit retrofit) {
        return retrofit.create(DigitalThanksApi.class);
    }

    @Provides
    @ThanksAnalyticsScope
    ThanksAnalyticsFactory provideThanksAnalyticsFactory(Gson gson,
                                                         DigitalThanksApi digitalThanksApi,
                                                         SessionHandler sessionHandler) {
        return new ThanksAnalyticsFactory(digitalThanksApi, gson, sessionHandler);
    }

    @Provides
    @ThanksAnalyticsScope
    ThanksAnalyticsRepository provideThanksAnalyticsRepository(ThanksAnalyticsFactory thanksAnalyticsFactory) {
        return new ThanksAnalyticsRepositoryImpl(thanksAnalyticsFactory);
    }

    @Provides
    @ThanksAnalyticsScope
    ThanksAnalyticsUseCase provideThanksAnalyticsUseCase(ThanksAnalyticsRepository thanksAnalyticsRepository) {
        return new ThanksAnalyticsUseCase(thanksAnalyticsRepository);
    }

    @Provides
    @ThanksAnalyticsScope
    ThanksAnalytics.Presenter provideThanksAnalyticsPresenter(ThanksAnalyticsUseCase thanksAnalyticsUseCase) {
        return new ThanksAnalyticsPresenter(thanksAnalyticsUseCase);
    }
}
