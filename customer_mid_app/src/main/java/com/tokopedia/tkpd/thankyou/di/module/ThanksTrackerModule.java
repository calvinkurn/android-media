package com.tokopedia.tkpd.thankyou.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.tkpd.thankyou.data.factory.ThanksTrackerFactory;
import com.tokopedia.tkpd.thankyou.data.mapper.DigitalTrackerMapper;
import com.tokopedia.tkpd.thankyou.data.repository.ThanksTrackerRepository;
import com.tokopedia.tkpd.thankyou.data.repository.ThanksTrackerRepositoryImpl;
import com.tokopedia.tkpd.thankyou.data.source.api.DigitalTrackerApi;
import com.tokopedia.tkpd.thankyou.data.source.api.DigitalTrackerService;
import com.tokopedia.tkpd.thankyou.di.scope.ThanksTrackerScope;
import com.tokopedia.tkpd.thankyou.domain.usecase.ThankYouPageTrackerUseCase;
import com.tokopedia.tkpd.thankyou.view.ThanksTracker;
import com.tokopedia.tkpd.thankyou.view.presenter.ThanksTrackerPresenter;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by okasurya on 12/4/17.
 */
@Module
public class ThanksTrackerModule {
    @Provides
    @ThanksTrackerScope
    DigitalTrackerService provideDigitalThanksService() {
        return new DigitalTrackerService();
    }

    @Provides
    @ThanksTrackerScope
    DigitalTrackerApi digitalThanksApi(DigitalTrackerService digitalTrackerService) {
        return digitalTrackerService.getApi();
    }

    @Provides
    @ThanksTrackerScope
    DigitalTrackerMapper digitalTrackerMapper(UserSessionInterface userSessionInterface) {
        return new DigitalTrackerMapper(userSessionInterface);
    }


    @Provides
    @ThanksTrackerScope
    UserSessionInterface userSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @Provides
    @ThanksTrackerScope
    ThanksTrackerFactory provideThanksAnalyticsFactory(DigitalTrackerApi digitalTrackerApi,
                                                       DigitalTrackerMapper digitalTrackerMapper,
                                                       @ApplicationContext Context context,
                                                       Gson gson,
                                                       UserSessionInterface userSessionInterface,
                                                       GCMHandler gcmHandler) {
        return new ThanksTrackerFactory(
                digitalTrackerApi,
                digitalTrackerMapper,
                context,
                gson,
                userSessionInterface,
                gcmHandler
        );
    }

    @Provides
    @ThanksTrackerScope
    ThanksTrackerRepository provideThanksAnalyticsRepository(ThanksTrackerFactory thanksTrackerFactory) {
        return new ThanksTrackerRepositoryImpl(thanksTrackerFactory);
    }

    @Provides
    @ThanksTrackerScope
    ThankYouPageTrackerUseCase provideThanksAnalyticsUseCase(ThanksTrackerRepository thanksTrackerRepository) {
        return new ThankYouPageTrackerUseCase(thanksTrackerRepository);
    }

    @Provides
    @ThanksTrackerScope
    ThanksTracker.Presenter provideThanksAnalyticsPresenter(@ApplicationContext Context context, ThankYouPageTrackerUseCase thankYouPageTrackerUseCase,
                                                            Gson gson) {
        return new ThanksTrackerPresenter(context, thankYouPageTrackerUseCase, gson);
    }
}
