package com.tokopedia.tkpd.tkpdcontactus.orderquery.domain;

import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.QueryTicket;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by sandeepgoyal on 12/04/18.
 */

public interface IQueryTicketRepository {
    Observable<List<QueryTicket>> getQueryTickets(HashMap<String, Object> id);
}
