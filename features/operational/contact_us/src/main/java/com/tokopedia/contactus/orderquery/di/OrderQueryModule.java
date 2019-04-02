package com.tokopedia.contactus.orderquery.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.contactus.home.source.api.ContactUsAPI;
import com.tokopedia.contactus.orderquery.domain.IQueryTicketRepository;
import com.tokopedia.contactus.orderquery.domain.ISubmitTicketRepository;
import com.tokopedia.contactus.orderquery.domain.QueryTicketUseCase;
import com.tokopedia.contactus.orderquery.domain.SubmitTicketUseCase;
import com.tokopedia.contactus.orderquery.source.api.OrderQueryApi;
import com.tokopedia.contactus.orderquery.source.queryticket.QueryTicketData;
import com.tokopedia.contactus.orderquery.source.queryticket.QueryTicketDataFactory;
import com.tokopedia.contactus.orderquery.source.submitticket.SubmitTicketFactory;
import com.tokopedia.contactus.orderquery.source.submitticket.SubmitTicketRepositoryImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by sandeepgoyal on 12/04/18.
 */
@Module
public class OrderQueryModule {
    @Provides
    IQueryTicketRepository provideQueryTicketRepository(QueryTicketDataFactory queryTicketDataFactory) {
        return new QueryTicketData(queryTicketDataFactory);
    }
    @Provides
    ISubmitTicketRepository provideSubmitTicketRepository(SubmitTicketFactory submitTicketFactory) {
        return new SubmitTicketRepositoryImpl(submitTicketFactory);
    }
    @Provides
    QueryTicketUseCase provideQueryTicketUseCase(IQueryTicketRepository queryTicketRepository) {
        return new QueryTicketUseCase(queryTicketRepository);
    }
    @Provides
    SubmitTicketUseCase provideSubmitTicketUseCase(ISubmitTicketRepository submitTicketRepository) {
        return new SubmitTicketUseCase(submitTicketRepository);
    }
    @Provides
    OrderQueryApi provide(Retrofit retrofit) {
        return retrofit.create(OrderQueryApi.class);
    }

    @Provides
    ContactUsAPI provideContactUs(Retrofit retrofit) {
        return retrofit.create(ContactUsAPI.class);
    }

    @Provides
    QueryTicketDataFactory provideQueryTicketFactory(OrderQueryApi api) {
        return new QueryTicketDataFactory(api);
    }
    @Provides
    SubmitTicketFactory provideSubmitTicketFactory(ContactUsAPI contactUsAPI, @ApplicationContext Context context) {
        return new SubmitTicketFactory(contactUsAPI, context);
    }
}
