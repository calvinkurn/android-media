package com.tokopedia.tkpd.tkpdcontactus.orderquery.source.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpd.tkpdcontactus.common.api.ContactUsURL;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.Solutions;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.SubmitTicketResponse;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * Created by sandeepgoyal on 17/04/18.
 */

public interface SubmitQueryAPi {
    @Multipart
    @POST(ContactUsURL.TICKET_SUBMIT_STEP_1)
    Observable<Response<DataResponse<SubmitTicketResponse>>> submitTicketDetails(@PartMap HashMap<String, RequestParams> param);

    @Multipart
    @POST(ContactUsURL.TICKET_SUBMIT_STEP_2)
    Observable<Response<DataResponse<SubmitTicketResponse>>> submitTicketDetails2(@PartMap Map<String, RequestBody> param);
}
