package com.tokopedia.tkpd.tkpdcontactus.orderquery.source.submitTicket;

import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.SubmitTicketResponse;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.domain.ISubmitTicketRepository;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by sandeepgoyal on 17/04/18.
 */

public class SubmitTicketData implements ISubmitTicketRepository {

    SubmitTicketFactory factory;

    public SubmitTicketData(SubmitTicketFactory factory) {
        this.factory = factory;
    }

    @Override
    public Observable<SubmitTicketResponse> submitTickets(HashMap<String, RequestParams> requestParamsHashMap) {
        return factory.getCloudStoreForTicketSubmit().submitTicket(requestParamsHashMap);
    }
}
