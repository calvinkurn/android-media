package com.tokopedia.tkpd.tkpdcontactus.orderquery.source.submitticket;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.ContactUsPass;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.SubmitTicketInvoiceData;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.domain.ISubmitTicketRepository;

import java.util.HashMap;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by baghira on 16/05/18.
 */

public class SubmitTicketRepositoryImpl implements ISubmitTicketRepository {
    SubmitTicketFactory submitTicketFactory;
    public SubmitTicketRepositoryImpl(SubmitTicketFactory submitTicketFactory) {
        this.submitTicketFactory = submitTicketFactory;
    }

    @Override
    public Observable<Response<TkpdResponse>> getQueryTickets(ContactUsPass ticket) {
        return submitTicketFactory.submitTicket(ticket);
    }
}
