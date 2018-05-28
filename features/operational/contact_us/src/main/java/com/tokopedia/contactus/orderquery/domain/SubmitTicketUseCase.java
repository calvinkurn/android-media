package com.tokopedia.contactus.orderquery.domain;

import com.tokopedia.contactus.orderquery.data.ContactUsPass;
import com.tokopedia.contactus.orderquery.data.CreateTicketResult;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by baghira on 16/05/18.
 */

public class SubmitTicketUseCase extends UseCase<CreateTicketResult> {
    ISubmitTicketRepository submitTicketRepository;
    public SubmitTicketUseCase(ISubmitTicketRepository submitTicketRepository) {
        this.submitTicketRepository = submitTicketRepository;
    }

    @Override
    public Observable<CreateTicketResult> createObservable(RequestParams requestParams) {

        return submitTicketRepository.getQueryTickets((ContactUsPass)requestParams.getObject("submitTicket"));
    }
}
