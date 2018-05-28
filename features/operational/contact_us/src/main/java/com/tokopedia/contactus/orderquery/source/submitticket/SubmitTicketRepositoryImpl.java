package com.tokopedia.contactus.orderquery.source.submitticket;

import com.tokopedia.contactus.orderquery.data.ContactUsPass;
import com.tokopedia.contactus.orderquery.data.CreateTicketResult;
import com.tokopedia.contactus.orderquery.domain.ISubmitTicketRepository;

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
    public Observable<CreateTicketResult> getQueryTickets(ContactUsPass ticket) {
        return submitTicketFactory.submitTicket(ticket);
    }
}
