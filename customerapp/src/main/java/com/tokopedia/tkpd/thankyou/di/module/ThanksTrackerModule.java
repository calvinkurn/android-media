package com.tokopedia.tkpd.thankyou.di.module;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.thankyou.data.factory.ThanksTrackerFactory;
import com.tokopedia.tkpd.thankyou.data.mapper.DigitalTrackerMapper;
import com.tokopedia.tkpd.thankyou.data.mapper.MarketplaceTrackerMapper;
import com.tokopedia.tkpd.thankyou.data.repository.ThanksTrackerRepository;
import com.tokopedia.tkpd.thankyou.data.repository.ThanksTrackerRepositoryImpl;
import com.tokopedia.tkpd.thankyou.data.source.DigitalTrackerCloudSource;
import com.tokopedia.tkpd.thankyou.data.source.MarketplaceTrackerCloudSource;
import com.tokopedia.tkpd.thankyou.data.source.api.DigitalTrackerApi;
import com.tokopedia.tkpd.thankyou.data.source.api.DigitalTrackerService;
import com.tokopedia.tkpd.thankyou.data.source.api.MarketplaceTrackerApi;
import com.tokopedia.tkpd.thankyou.data.source.api.MarketplaceTrackerService;
import com.tokopedia.tkpd.thankyou.di.scope.ThanksTrackerScope;
import com.tokopedia.tkpd.thankyou.domain.usecase.ThankYouPageTrackerUseCase;
import com.tokopedia.tkpd.thankyou.view.ThanksTracker;
import com.tokopedia.tkpd.thankyou.view.presenter.ThanksTrackerPresenter;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by okasurya on 12/4/17.
 */
@Module
@ThanksTrackerScope
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
    DigitalTrackerMapper digitalTrackerMapper(SessionHandler sessionHandler) {
        return new DigitalTrackerMapper(sessionHandler);
    }

    @Provides
    @ThanksTrackerScope
    MarketplaceTrackerService provideMarketplaceTrackerService() {
        return new MarketplaceTrackerService();
    }

    @Provides
    @ThanksTrackerScope
    MarketplaceTrackerApi provideMarketplaceTrackerApi(MarketplaceTrackerService service) {
        return service.getApi();
    }

    @Provides
    @ThanksTrackerScope
    MarketplaceTrackerMapper provideMarketplaceTrackerMapper(SessionHandler sessionHandler) {
        return new MarketplaceTrackerMapper(sessionHandler);
    }

    @Provides
    @ThanksTrackerScope
    ThanksTrackerFactory provideThanksAnalyticsFactory(DigitalTrackerApi digitalTrackerApi,
                                                       DigitalTrackerMapper digitalTrackerMapper,
                                                       MarketplaceTrackerApi marketplaceTrackerApi,
                                                       MarketplaceTrackerMapper marketplaceTrackerMapper,
                                                       @ApplicationContext Context context,
                                                       Gson gson,
                                                       SessionHandler sessionHandler,
                                                       GCMHandler gcmHandler) {
        return new ThanksTrackerFactory(
                digitalTrackerApi,
                digitalTrackerMapper,
                marketplaceTrackerApi,
                marketplaceTrackerMapper,
                context,
                gson,
                sessionHandler,
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
    ThanksTracker.Presenter provideThanksAnalyticsPresenter(ThankYouPageTrackerUseCase thankYouPageTrackerUseCase) {
        return new ThanksTrackerPresenter(thankYouPageTrackerUseCase);
    }
}
