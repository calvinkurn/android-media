package com.tokopedia.contactus.orderquery.source.queryticket;

import com.tokopedia.contactus.orderquery.data.QueryTicket;
import com.tokopedia.contactus.orderquery.domain.IQueryTicketRepository;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by sandeepgoyal on 12/04/18.
 */

public class QueryTicketData implements IQueryTicketRepository {


    QueryTicketDataFactory queryTicketDataFactory;

    public QueryTicketData(QueryTicketDataFactory queryTicketDataFactory) {
        this.queryTicketDataFactory = queryTicketDataFactory;
    }

    @Override
    public Observable<List<QueryTicket>> getQueryTickets(HashMap<String, Object> id) {
        return queryTicketDataFactory.getCloutTicketRepository().getQueryTickets(id);
    }
}
