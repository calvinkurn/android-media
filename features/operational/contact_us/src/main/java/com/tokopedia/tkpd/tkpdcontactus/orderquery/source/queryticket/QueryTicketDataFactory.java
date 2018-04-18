package com.tokopedia.tkpd.tkpdcontactus.orderquery.source.queryticket;

import com.tokopedia.tkpd.tkpdcontactus.orderquery.source.api.OrderQueryApi;

/**
 * Created by sandeepgoyal on 12/04/18.
 */

public class QueryTicketDataFactory {

    OrderQueryApi orderQueryApi;

    public QueryTicketDataFactory(OrderQueryApi orderQueryApi) {
        this.orderQueryApi = orderQueryApi;
    }

    public QueryTicketDataStore getCloutTicketRepository() {
        return new QueryTicketDataStore(orderQueryApi);
    }
}
