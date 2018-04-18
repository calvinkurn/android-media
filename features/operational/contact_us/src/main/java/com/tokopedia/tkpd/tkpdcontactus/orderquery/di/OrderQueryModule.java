package com.tokopedia.tkpd.tkpdcontactus.orderquery.di;

import com.tokopedia.tkpd.tkpdcontactus.orderquery.domain.IQueryTicketRepository;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.domain.QueryTicketUseCase;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.source.queryticket.QueryTicketData;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.source.queryticket.QueryTicketDataFactory;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.source.api.OrderQueryApi;

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
    QueryTicketUseCase provideQueryTicketUseCase(IQueryTicketRepository queryTicketRepository) {
        return new QueryTicketUseCase(queryTicketRepository);
    }
    @Provides
    OrderQueryApi provide(Retrofit retrofit) {
        return retrofit.create(OrderQueryApi.class);
    }
    @Provides
    QueryTicketDataFactory provideQueryTicketFactory(OrderQueryApi api) {
        return new QueryTicketDataFactory(api);
    }
}
