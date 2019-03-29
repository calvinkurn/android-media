package com.tokopedia.contactus.orderquery.domain;

import com.tokopedia.contactus.orderquery.data.QueryTicket;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * Created by sandeepgoyal on 12/04/18.
 */

public class QueryTicketUseCase extends UseCase<List<QueryTicket>> {
    IQueryTicketRepository repository;


    public QueryTicketUseCase(IQueryTicketRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<List<QueryTicket>> createObservable(RequestParams requestParams) {
        // TODO Need to   update
        return repository.getQueryTickets(requestParams.getParameters());
    }
}
