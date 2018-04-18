package com.tokopedia.tkpd.tkpdcontactus.orderquery.source.submitTicket;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.SubmitTicketResponse;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.source.api.SubmitQueryAPi;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sandeepgoyal on 17/04/18.
 */

public class SubmitTicketDataStore {
    SubmitQueryAPi submitQueryAPi;

    public SubmitTicketDataStore(SubmitQueryAPi submitQueryAPi) {
        this.submitQueryAPi = submitQueryAPi;
    }

    public Observable<SubmitTicketResponse> submitTicket(HashMap<String,RequestParams> params) {
        return submitQueryAPi.submitTicketDetails(params).map(new Func1<Response<DataResponse<SubmitTicketResponse>>, SubmitTicketResponse>() {
            @Override
            public SubmitTicketResponse call(Response<DataResponse<SubmitTicketResponse>> dataResponseResponse) {
                return dataResponseResponse.body().getData();
            }
        });

    }
}
