package com.tokopedia.events.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.events.EventModuleRouter;
import com.tokopedia.events.data.EventRepositoryData;
import com.tokopedia.events.data.EventsDataStoreFactory;
import com.tokopedia.events.data.source.EventsApi;
import com.tokopedia.events.data.source.EventsUrl;
import com.tokopedia.events.di.scope.EventScope;
import com.tokopedia.events.domain.EventRepository;
import com.tokopedia.events.domain.GetEventDetailsRequestUseCase;
import com.tokopedia.events.domain.GetEventSeatLayoutUseCase;
import com.tokopedia.events.domain.GetEventsListByLocationRequestUseCase;
import com.tokopedia.events.domain.GetEventsListRequestUseCase;
import com.tokopedia.events.domain.GetEventsLocationListRequestUseCase;
import com.tokopedia.events.domain.GetProductRatingUseCase;
import com.tokopedia.events.domain.GetSearchEventsListRequestUseCase;
import com.tokopedia.events.domain.GetSearchNextUseCase;
import com.tokopedia.events.domain.GetUserLikesUseCase;
import com.tokopedia.events.domain.postusecase.CheckoutPaymentUseCase;
import com.tokopedia.events.domain.postusecase.PostInitCouponUseCase;
import com.tokopedia.events.domain.postusecase.PostNsqEventUseCase;
import com.tokopedia.events.domain.postusecase.PostNsqTravelDataUseCase;
import com.tokopedia.events.domain.postusecase.PostUpdateEventLikesUseCase;
import com.tokopedia.events.domain.postusecase.PostValidateShowUseCase;
import com.tokopedia.events.domain.postusecase.VerifyCartUseCase;
import com.tokopedia.events.domain.scanTicketUsecase.CheckScanOptionUseCase;
import com.tokopedia.events.domain.scanTicketUsecase.RedeemTicketUseCase;
import com.tokopedia.events.domain.scanTicketUsecase.ScanBarCodeUseCase;
import com.tokopedia.events.view.contractor.EventBaseContract;
import com.tokopedia.events.view.contractor.EventBookTicketContract;
import com.tokopedia.events.view.contractor.EventFavouriteContract;
import com.tokopedia.events.view.contractor.EventFilterContract;
import com.tokopedia.events.view.contractor.EventReviewTicketsContractor;
import com.tokopedia.events.view.contractor.EventSearchContract;
import com.tokopedia.events.view.contractor.EventsContract;
import com.tokopedia.events.view.contractor.EventsDetailsContract;
import com.tokopedia.events.view.contractor.ScanCodeContract;
import com.tokopedia.events.view.contractor.SeatSelectionContract;
import com.tokopedia.events.view.customview.SelectEventDateBottomSheet;
import com.tokopedia.events.view.presenter.EventBookTicketPresenter;
import com.tokopedia.events.view.presenter.EventFavouritePresenter;
import com.tokopedia.events.view.presenter.EventFilterPresenterImpl;
import com.tokopedia.events.view.presenter.EventHomePresenter;
import com.tokopedia.events.view.presenter.EventLocationsPresenter;
import com.tokopedia.events.view.presenter.EventReviewTicketPresenter;
import com.tokopedia.events.view.presenter.EventSearchPresenter;
import com.tokopedia.events.view.presenter.EventsDetailsPresenter;
import com.tokopedia.events.view.presenter.ScanCodeDataPresenter;
import com.tokopedia.events.view.presenter.SeatSelectionPresenter;
import com.tokopedia.events.view.utils.EventsAnalytics;
import com.tokopedia.events.view.utils.VerifyCartWrapper;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.oms.domain.postusecase.PostPaymentUseCase;
import com.tokopedia.oms.domain.postusecase.PostVerifyCartUseCase;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by ashwanityagi on 03/11/17.
 */

@Module
public class EventModule {

    private static final int NET_READ_TIMEOUT = 60;
    private static final int NET_WRITE_TIMEOUT = 60;
    private static final int NET_CONNECT_TIMEOUT = 60;
    private static final int NET_RETRY = 1;

    Context thisContext;

    public EventModule(Context context) {
        thisContext = context;
    }

    @Provides
    EventsApi provideEventsApi(@EventQualifier Retrofit retrofit) {
        return retrofit.create(EventsApi.class);
    }

    @Provides
    @EventScope
    EventsDataStoreFactory provideEventsDataStoreFactory(EventsApi eventApi) {
        return new EventsDataStoreFactory(eventApi);
    }

    @Provides
    @EventScope
    EventRepository provideEventRepository(EventsDataStoreFactory eventsDataStoreFactory) {
        return new EventRepositoryData(eventsDataStoreFactory);
    }

    @Provides
    @EventScope
    GetEventsListRequestUseCase provideGetEventsListRequestUseCase(EventRepository eventRepository) {
        return new GetEventsListRequestUseCase(eventRepository);
    }

    @Provides
    @EventScope
    @EventQualifier
    Retrofit provideEventRetrofit(OkHttpClient okHttpClient,
                                  Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(EventsUrl.EVENTS_DOMAIN).client(okHttpClient).build();
    }

    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            HeaderErrorResponseInterceptor errorResponseInterceptor, @ApplicationContext Context context) {
        UserSession userSession = new UserSession(context);
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(new TkpdAuthInterceptor(context, (NetworkRouter) context, userSession))
                .addInterceptor(new FingerprintInterceptor((NetworkRouter) context, userSession))
                .addInterceptor(((EventModuleRouter) context).getChuckerInterceptor())
                .build();
    }


    @Provides
    @EventScope
    GetEventsLocationListRequestUseCase provideGetEventsLocationListRequestUseCase(EventRepository eventRepository) {
        return new GetEventsLocationListRequestUseCase(eventRepository);
    }

    @Provides
    @EventScope
    GetEventsListByLocationRequestUseCase provideGetEventsListByLocationRequestUseCase(EventRepository eventRepository) {
        return new GetEventsListByLocationRequestUseCase(eventRepository);
    }

    @Provides
    @EventScope
    GetEventDetailsRequestUseCase provideGetEventDetailsRequestUseCase(EventRepository eventRepository) {
        return new GetEventDetailsRequestUseCase(eventRepository);
    }

    @Provides
    @EventScope
    GetSearchEventsListRequestUseCase provideGetSearchEventsListRequestUseCase(EventRepository eventRepository) {
        return new GetSearchEventsListRequestUseCase(eventRepository);
    }

    @Provides
    @EventScope
    PostValidateShowUseCase providesPostValidateShowUseCase(EventRepository eventRepository) {
        return new PostValidateShowUseCase(eventRepository);
    }

    @Provides
    @EventScope
    VerifyCartUseCase provideVerifyCartUseCase(EventRepository eventRepository) {
        return new VerifyCartUseCase(eventRepository);
    }

    @Provides
    @EventScope
    GetEventSeatLayoutUseCase providesGetEventSeatLayoutUseCase(EventRepository eventRepository) {
        return new GetEventSeatLayoutUseCase(eventRepository);
    }

    @Provides
    @EventScope
    GetSearchNextUseCase providesGetSearchNextUseCase(EventRepository eventRepository) {
        return new GetSearchNextUseCase(eventRepository);
    }

    @Provides
    @EventScope
    Context getActivityContext() {
        return thisContext;
    }

    @Provides
    @EventScope
    VerifyCartWrapper providesVerifyCartWrapper(VerifyCartUseCase useCase) {
        return new VerifyCartWrapper(useCase);
    }


    @Provides
    @EventScope
    EventFilterContract.EventFilterPresenter providesEventFilterPresenter() {
        return new EventFilterPresenterImpl();
    }

    @Provides
    @EventScope
    EventBookTicketContract.BookTicketPresenter providesEventBookTicketPresenter(GetEventSeatLayoutUseCase seatLayoutUseCase,
                                                                                 PostValidateShowUseCase postValidateShowUseCase, EventsAnalytics eventsAnalytics) {
        return new EventBookTicketPresenter(seatLayoutUseCase, postValidateShowUseCase, eventsAnalytics);
    }

    @Provides
    @EventScope
    EventsDetailsContract.EventDetailPresenter providesEventsDetailsPresenter(GetEventDetailsRequestUseCase getEventDetailsRequestUseCase, EventsAnalytics eventsAnalytics, CheckScanOptionUseCase checkScanOptionUseCase, PostNsqEventUseCase postNsqEventUseCase, PostNsqTravelDataUseCase postNsqTravelDataUseCase) {
        return new EventsDetailsPresenter(getEventDetailsRequestUseCase, eventsAnalytics, checkScanOptionUseCase, postNsqEventUseCase, postNsqTravelDataUseCase);
    }

    @Provides
    @EventScope
    EventFavouriteContract.EventFavoritePresenter providesEventFavouritePresenter(GetUserLikesUseCase getUserLikesUseCase,
                                                                                  PostUpdateEventLikesUseCase eventLikesUseCase, EventsAnalytics eventsAnalytics) {
        return new EventFavouritePresenter(getUserLikesUseCase, eventLikesUseCase, eventsAnalytics);
    }

    @Provides
    @EventScope
    EventLocationsPresenter providesEventLocationsPresenter(GetEventsLocationListRequestUseCase getEventsLocationListRequestUseCase) {
        return new EventLocationsPresenter(getEventsLocationListRequestUseCase);
    }


    @Provides
    @EventScope
    EventSearchContract.EventSearchPresenter providesEventSearchPresenter(GetSearchEventsListRequestUseCase getSearchEventsListRequestUseCase,
                                                                          GetSearchNextUseCase searchNextUseCase, PostUpdateEventLikesUseCase eventLikesUseCase, EventsAnalytics eventsAnalytics) {
        return new EventSearchPresenter(getSearchEventsListRequestUseCase,
                searchNextUseCase, eventLikesUseCase, eventsAnalytics);
    }

    @Provides
    @EventScope
    EventsContract.EventHomePresenter providesEventHomePresenter(GetEventsListRequestUseCase getEventsListRequestUsecase,
                                                                 PostUpdateEventLikesUseCase eventLikesUseCase,
                                                                 GetUserLikesUseCase likesUseCase,
                                                                 GetProductRatingUseCase ratingUseCase, PostNsqEventUseCase postNsqEventUseCase, EventsAnalytics eventsAnalytics) {
        return new EventHomePresenter(getEventsListRequestUsecase,
                eventLikesUseCase, likesUseCase, ratingUseCase, postNsqEventUseCase, eventsAnalytics);
    }


    @Provides
    @EventScope
    EventReviewTicketsContractor.EventReviewTicketPresenter providesReviewTicketPresenter(VerifyCartUseCase usecase, CheckoutPaymentUseCase payment,
                                                                                          PostInitCouponUseCase couponUseCase, PostVerifyCartUseCase postVerifyCartUseCase, PostPaymentUseCase postPaymentUseCase, EventsAnalytics eventsAnalytics) {
        return new EventReviewTicketPresenter(usecase,
                payment, couponUseCase, postVerifyCartUseCase, postPaymentUseCase, eventsAnalytics);
    }

    @Provides
    @EventScope
    SeatSelectionContract.SeatSelectionPresenter providesSeatSelectionPresenter(VerifyCartUseCase verifyCartUseCase,
                                                                                PostVerifyCartUseCase postVerifyCartUseCase) {
        return new SeatSelectionPresenter(verifyCartUseCase, postVerifyCartUseCase);
    }

    @Provides
    @EventScope
    ScanCodeContract.ScanPresenter providesScanCodePresenter(ScanBarCodeUseCase scanBarCodeUseCase, RedeemTicketUseCase redeemTicketUseCase) {
        return new ScanCodeDataPresenter(scanBarCodeUseCase, redeemTicketUseCase);
    }

    @Provides
    @EventScope
    SelectEventDateBottomSheet providesSelectEventDateBottomSheet() {
        return new SelectEventDateBottomSheet();
    }
}
