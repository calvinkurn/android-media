package com.tokopedia.contactus.orderquery.source.queryticket;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.contactus.orderquery.data.QueryTicket;
import com.tokopedia.contactus.orderquery.data.Solutions;
import com.tokopedia.contactus.orderquery.source.api.OrderQueryApi;

import java.util.HashMap;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sandeepgoyal on 12/04/18.
 */

public class QueryTicketDataStore {
    OrderQueryApi orderQueryApi;

    public QueryTicketDataStore(OrderQueryApi orderQueryApi) {
        this.orderQueryApi = orderQueryApi;
    }

    public Observable<List<QueryTicket>> getQueryTickets(HashMap<String, Object> id) {
        return this.orderQueryApi.getTicketOptions(id).map(new Func1<Response<DataResponse<Solutions>>, List<QueryTicket>>() {
            @Override
            public List<QueryTicket> call(Response<DataResponse<Solutions>> dataResponseResponse) {
                return dataResponseResponse.body().getData().getSolutions();
            }
        });
    }
}
