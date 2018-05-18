package com.tokopedia.tkpd.tkpdcontactus.orderquery.di;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.apis.AccountsApi;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.tkpd.tkpdcontactus.home.source.api.ContactUsAPI;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.domain.IQueryTicketRepository;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.domain.ISubmitTicketRepository;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.domain.QueryTicketUseCase;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.domain.SubmitTicketUseCase;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.source.queryticket.QueryTicketData;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.source.queryticket.QueryTicketDataFactory;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.source.api.OrderQueryApi;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.source.submitticket.SubmitTicketDataStore;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.source.submitticket.SubmitTicketFactory;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.source.submitticket.SubmitTicketRepositoryImpl;

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
    AccountsApi provideAccountsApi(Retrofit retrofit) {
        return retrofit.create(AccountsApi.class);
    }
    @Provides
    QueryTicketDataFactory provideQueryTicketFactory(OrderQueryApi api) {
        return new QueryTicketDataFactory(api);
    }
    @Provides
    SubmitTicketFactory provideSubmitTicketFactory(ContactUsAPI contactUsAPI, AccountsApi accountsApi, @ApplicationContext Context context) {
        return new SubmitTicketFactory(contactUsAPI, accountsApi, context);
    }
}
