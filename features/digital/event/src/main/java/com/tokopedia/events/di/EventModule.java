package com.tokopedia.events.di;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.ProfileSourceFactory;
import com.tokopedia.core.drawer2.data.mapper.ProfileMapper;
import com.tokopedia.core.drawer2.data.repository.ProfileRepositoryImpl;
import com.tokopedia.core.drawer2.domain.ProfileRepository;
import com.tokopedia.core.drawer2.domain.interactor.ProfileUseCase;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.retrofit.interceptors.EventInerceptors;
import com.tokopedia.events.EventModuleRouter;
import com.tokopedia.events.data.EventRepositoryData;
import com.tokopedia.events.data.EventsDataStoreFactory;
import com.tokopedia.events.data.source.EventsApi;
import com.tokopedia.events.di.scope.EventScope;
import com.tokopedia.events.domain.EventRepository;
import com.tokopedia.events.domain.GetEventDetailsRequestUseCase;
import com.tokopedia.events.domain.GetEventSeatLayoutUseCase;
import com.tokopedia.events.domain.GetEventsListByLocationRequestUseCase;
import com.tokopedia.events.domain.GetEventsListRequestUseCase;
import com.tokopedia.events.domain.GetEventsLocationListRequestUseCase;
import com.tokopedia.events.domain.GetSearchEventsListRequestUseCase;
import com.tokopedia.events.domain.GetSearchNextUseCase;
import com.tokopedia.events.domain.postusecase.PostValidateShowUseCase;
import com.tokopedia.events.domain.postusecase.VerifyCartUseCase;
import com.tokopedia.events.view.contractor.EventBaseContract;
import com.tokopedia.events.view.presenter.EventBookTicketPresenter;
import com.tokopedia.events.view.presenter.EventFilterPresenterImpl;
import com.tokopedia.events.view.utils.VerifyCartWrapper;

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
    @EventScope
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
    GetEventsListRequestUseCase provideGetEventsListRequestUseCase(ThreadExecutor threadExecutor,
                                                                   PostExecutionThread postExecutionThread,
                                                                   EventRepository eventRepository) {
        return new GetEventsListRequestUseCase(eventRepository);
    }

    @Provides
    ThreadExecutor provideThreadExecutor() {
        return new JobExecutor();
    }

    @Provides
    PostExecutionThread providePostExecutionThread() {
        return new UIThread();
    }

    @Provides
    @EventQualifier
    @EventScope
    Retrofit provideEventRetrofit(OkHttpClient okHttpClient,
                                  Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.EVENTS_DOMAIN).client(okHttpClient).build();
    }

    @Provides
    OkHttpClient provideOkHttp(EventInerceptors eventInerceptors, HttpLoggingInterceptor loggingInterceptor) {
        return ((EventModuleRouter) thisContext.getApplicationContext()).getOkHttpClient(eventInerceptors, loggingInterceptor);
    }

    @Provides
    @EventQualifier
    @EventScope
    OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(
                NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY
        );
    }


    @EventScope
    @Provides
    EventInerceptors provideEventInterCeptor(Context context) {
        String oAuthString = "Bearer " + ((EventModuleRouter) thisContext.getApplicationContext()).getSession().getAccessToken();
        return new EventInerceptors(oAuthString, context);
    }

    @Provides
    @EventScope
    GetEventsLocationListRequestUseCase provideGetEventsLocationListRequestUseCase(ThreadExecutor threadExecutor,
                                                                                   PostExecutionThread postExecutionThread,
                                                                                   EventRepository eventRepository) {
        return new GetEventsLocationListRequestUseCase(eventRepository);
    }

    @Provides
    @EventScope
    GetEventsListByLocationRequestUseCase provideGetEventsListByLocationRequestUseCase(ThreadExecutor threadExecutor,
                                                                                       PostExecutionThread postExecutionThread,
                                                                                       EventRepository eventRepository) {
        return new GetEventsListByLocationRequestUseCase(eventRepository);
    }

    @Provides
    @EventScope
    GetEventDetailsRequestUseCase provideGetEventDetailsRequestUseCase(ThreadExecutor threadExecutor,
                                                                       PostExecutionThread postExecutionThread,
                                                                       EventRepository eventRepository) {
        return new GetEventDetailsRequestUseCase(eventRepository);
    }

    @Provides
    @EventScope
    GetSearchEventsListRequestUseCase provideGetSearchEventsListRequestUseCase(ThreadExecutor threadExecutor,
                                                                               PostExecutionThread postExecutionThread,
                                                                               EventRepository eventRepository) {
        return new GetSearchEventsListRequestUseCase(eventRepository);
    }

    @Provides
    @EventScope
    PostValidateShowUseCase providesPostValidateShowUseCase(ThreadExecutor threadExecutor,
                                                            PostExecutionThread postExecutionThread,
                                                            EventRepository eventRepository) {
        return new PostValidateShowUseCase(eventRepository);
    }

    @Provides
    @EventScope
    VerifyCartUseCase provideVerifyCartUseCase(EventRepository eventRepository) {
        return new VerifyCartUseCase(eventRepository);
    }

    @Provides
    @EventScope
    GetEventSeatLayoutUseCase providesGetEventSeatLayoutUseCase(ThreadExecutor threadExecutor,
                                                                PostExecutionThread postExecutionThread,
                                                                EventRepository eventRepository) {
        return new GetEventSeatLayoutUseCase(eventRepository);
    }

    @Provides
    @EventScope
    GetSearchNextUseCase providesGetSearchNextUseCase(ThreadExecutor threadExecutor,
                                                      PostExecutionThread postExecutionThread,
                                                      EventRepository eventRepository) {
        return new GetSearchNextUseCase(eventRepository);
    }

    @Provides
    @EventScope
    Context getActivityContext() {
        return thisContext;
    }

    @Provides
    @EventScope
    ProfileUseCase providesProfileUseCase(ThreadExecutor threadExecutor,
                                          PostExecutionThread postExecutionThread,
                                          ProfileRepository profileRepository) {
        return new ProfileUseCase(
                threadExecutor, postExecutionThread, profileRepository);
    }

    @Provides
    @EventScope
    VerifyCartWrapper providesVerifyCartWrapper(VerifyCartUseCase useCase) {
        return new VerifyCartWrapper(useCase);
    }


    @Provides
    @EventScope
    EventBaseContract.EventBasePresenter providesEventFilterPresenter() {
        return new EventFilterPresenterImpl();
    }

    @Provides
    @EventScope
    EventBaseContract.EventBasePresenter providesEventBookTicketPresenter(GetEventSeatLayoutUseCase seatLayoutUseCase,
                                                                          PostValidateShowUseCase postValidateShowUseCase) {
        return new EventBookTicketPresenter(seatLayoutUseCase, postValidateShowUseCase);
    }

}
