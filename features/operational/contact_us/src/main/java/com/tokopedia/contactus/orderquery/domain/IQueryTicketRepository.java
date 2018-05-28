package com.tokopedia.contactus.orderquery.domain;

import com.tokopedia.contactus.orderquery.data.QueryTicket;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by sandeepgoyal on 12/04/18.
 */

public interface IQueryTicketRepository {
    Observable<List<QueryTicket>> getQueryTickets(HashMap<String, Object> id);
}
