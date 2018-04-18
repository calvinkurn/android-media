package com.tokopedia.tkpd.tkpdcontactus.orderquery.di;

import com.tokopedia.tkpd.tkpdcontactus.orderquery.domain.IQueryTicketRepository;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.domain.ISubmitTicketRepository;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.domain.QueryTicketUseCase;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.domain.SubmitTicketUsecase;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.source.api.OrderQueryApi;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.source.api.SubmitQueryAPi;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.source.queryticket.QueryTicketData;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.source.queryticket.QueryTicketDataFactory;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.source.submitTicket.SubmitTicketData;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.source.submitTicket.SubmitTicketFactory;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by sandeepgoyal on 17/04/18.
 */

@Module
public class SubmitQueryModule {
    @Provides
    ISubmitTicketRepository provideSubmitTicketRepository(SubmitTicketFactory queryTicketDataFactory) {
        return new SubmitTicketData(queryTicketDataFactory);
    }
    @Provides
    SubmitTicketUsecase provideSubmitTicketUseCase(ISubmitTicketRepository submitTicketRepository) {
        return new SubmitTicketUsecase(submitTicketRepository);
    }
    @Provides
    SubmitQueryAPi provide(Retrofit retrofit) {
        return retrofit.create(SubmitQueryAPi.class);
    }
    @Provides
    SubmitTicketFactory provideSubmitTicketFactory(SubmitQueryAPi api) {
        return new SubmitTicketFactory(api);
    }
}
