package com.tokopedia.tkpd.tkpdcontactus.orderquery.domain;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.SubmitTicketInvoiceData;

import java.util.HashMap;
import java.util.List;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by baghira on 16/05/18.
 */

public interface ISubmitTicketRepository {
    Observable<Response<TkpdResponse>> getQueryTickets(HashMap<String, Object> id);
}
