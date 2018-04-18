package com.tokopedia.tkpd.tkpdcontactus.orderquery.domain;

import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.QueryTicket;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.SubmitTicketResponse;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by sandeepgoyal on 17/04/18.
 */

public interface ISubmitTicketRepository {
    Observable<SubmitTicketResponse> submitTickets(HashMap<String, RequestParams> id);
}
